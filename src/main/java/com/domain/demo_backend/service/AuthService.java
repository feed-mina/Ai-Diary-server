package com.domain.demo_backend.service;


import com.domain.demo_backend.mapper.UserMapper;
import com.domain.demo_backend.user.domain.User;
import com.domain.demo_backend.user.dto.LoginRequest;
import com.domain.demo_backend.user.dto.RegisterRequest;
import com.domain.demo_backend.util.JwtUtil;
import com.domain.demo_backend.util.PasswordUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;


    public AuthService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }
    public String login(LoginRequest loginRequest) {

        User user = userMapper.findByUserId(loginRequest.getUserId());
        // 디버깅
        System.out.println("DB에서 가져온 User: " + user);
        System.out.println("LoginRequest UserID: " + loginRequest.getUserId());
        System.out.println("DB 해시값: " + user.getHashedPassword());
        System.out.println("입력된 해시값: " + PasswordUtil.sha256(loginRequest.getPassword()));

        if (user == null) {
            System.out.println("아이디 실패");
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }
        if (!user.getHashedPassword().equals(PasswordUtil.sha256(loginRequest.getPassword()))) {
            System.out.println("비밀번호 실패");
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        System.out.println("JWT 성공");
        // 비밀번호를 포함하지 않은 사용자 정보를 JWT에 포함
        return jwtUtil.createToken( user.getUsername(), user.getUserSqno(), user.getUserId());
    }

    public class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String message) {
            super(message);
        }
    }

    // 새 사용자 정보를 해시처리 후 데이터베이스에 저장
    // 이미 존재하는 사용자 아이디인지 확인하고 중복되면 예외 발생
    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userMapper.findByUserEmail(registerRequest.getEmail()) != null) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
            if (userMapper.findByUserId(registerRequest.getUserId()) != null) {
            System.out.println("존재하는 아이디 실패");

            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if(userMapper.findByUserEmail(registerRequest.getEmail()) != null){
            System.out.println("회원가입 이메일 실패");

            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if(userMapper.findByUserPhone(registerRequest.getPhone()) != null){
            System.out.println("회원가입 핸드폰 실패");
            throw new IllegalArgumentException("이미 존재하는 핸드폰 번호입니다.");
        }

        System.out.println("유효성 통과");
        User user = User.builder()
                .userId(registerRequest.getUserId())
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .hashedPassword(PasswordUtil.sha256(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .email(registerRequest.getEmail())
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();
        System.out.println("user: "+ user);
        System.out.println("user Mapper insertUser 시작");
        userMapper.insertUser(user);
    }


    // 새 사용자 정보를 해시처리 후 데이터베이스에 저장
    // 이미 존재하는 사용자 아이디인지 확인하고 중복되면 예외 발생
    @Transactional
    public void nonMember(RegisterRequest registerRequest) {
        System.out.println("회원탈퇴 서비스 진입: " + registerRequest.getUserId());
        User existingUser = userMapper.findByUserId(registerRequest.getUserId());
        if (existingUser == null) {
            System.out.println("회원탈퇴 실패: 해당 사용자가 존재하지 않습니다.");
            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
        }
        // 회원탈퇴 처리
        existingUser.setDelYn("Y");
        existingUser.setUpdatedAt(LocalDateTime.now());
        System.out.println("existingUser : "+ existingUser);
        System.out.println("user Mapper nonMember 시작");
        userMapper.nonMember(existingUser);
        System.out.println("user 탈퇴 처리 완료: " + existingUser);
    }


    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String body, String imagePath) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        // Inline 이미지 추가
        File imageFile = new File(imagePath);
        helper.addInline("imageId", imageFile);

        mailSender.send(mimeMessage);
    }
    public void sendEmailWithAttachment(String to, String subject, String body, String filePath) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true는 첨부 파일 허용

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // HTML 형식 허용

        // 첨부 파일 추가
        File file = new File(filePath);
        helper.addAttachment(file.getName(), file);

        mailSender.send(mimeMessage);
    }
    public void saveVerificationCode(String email, String verificationCode){
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(60);
        userMapper.insertVerification(email,verificationCode,expiresAt);
    }

    public boolean verifyCode(String email, String code){
        String storedCode = userMapper.getVerificationCode(email);
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(60);
        if(storedCode != null && storedCode.equals(code)&& expirationTime.isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }
    public String sendVerificationCode(String email) throws MessagingException {
        //랜덤 인등코드 생성
        String verificationCode = generateRendomCode();
        // 이메일 작성 및 전송
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(email);
        helper.setSubject("이메일 인증 코드");
        helper.setText("인증 코드: " + verificationCode, true);

        mailSender.send(message);

        return verificationCode; // 인증 코드 반환

    }
    private String generateRendomCode() {
        Random random = new Random();
        int code = 1000000 + random.nextInt(10000);
        // 랜덤 6자리 숫자 생성
        return String.valueOf(code);
    }


}
