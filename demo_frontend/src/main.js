import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// main.js 또는 필요한 Vue 컴포넌트
import 'tailwindcss/tailwind.css';

createApp(App)
    .use(router)
    .mount('#app');