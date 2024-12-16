// login.js


const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
})

async function postLogin(username, password) {
    try {
        const response = await apiClient.post('/auth/userLogin', {
            username: username,
            password: password
        });


        if (!response.status === 200) {
            // JWT 토큰을 로컬 스토리지에 저장
            const token = response.data.token;
            localStorage.setItem("jwtToken", token);
            alert('로그인 성공!');
            await fetchDiscountInfo();
        } else {
            alert("로그인 실패");
        }

    } catch (error) {
        console.error('로그인 오류:', error);
        alert('로그인 중 문제가 발생했습니다. ');
    }
}