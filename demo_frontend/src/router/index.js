import { createRouter, createWebHistory } from 'vue-router'

import Dashboard from '@/views/Dashboard.vue'
import AssetDetail from '@/views/AssetDetail.vue'
import DiscountInfo from '@/views/DiscountInfo.vue'
import Asset from '@/views/Asset.vue'
import NotFound from '@/components/NotFound.vue'
import UserLogin from '@/views/UserLogin.vue'

// 라우터 설정
const routes = [{
        path: '/userLogin',
        name: 'UserLogin',
        component: UserLogin
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard
    },
    {
        path: '/asset',
        name: 'Asset',
        component: Asset
    },
    {
        path: '/assets/:id', // 자산 상세 페이지는 자산 Id에 따라 달라질
        name: 'AssetDetail',
        component: AssetDetail,
        props: true // URL의 :id를 props로 전달
    },
    {
        path: '/discount',
        name: 'DiscountInfo',
        component: DiscountInfo
    },
    {
        path: '/:pathMatch(.*)*', // 어떤 경로에도 매칭되지 않는 경우 
        name: 'NotFound',
        component: NotFound
    }
]

// 라우터 생성
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;