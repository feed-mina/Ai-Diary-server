// tailwind.config.js
module.exports = {
    purge: [
        './src/**/*.vue',
        './public/index.html',
        // 추가적으로 제거하지 않을 파일 경로를 지정할 수 있습니다.
    ],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {
            colors: {
                'custom-blue': '#3490dc',
            },
            fontFamily: {
                sans: ['Roboto', 'Arial', 'sans-serif'],
            },
        },
    },
    variants: {
        extend: {
            opacity: ['disabled'], // disabled 상태에서도 opacity를 조절할 수 있도록 확장
        },
    },
    plugins: [
        // 예시: Tailwind CSS Typography 플러그인 추가
        require('@tailwindcss/typography'),
    ],
}