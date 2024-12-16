<template>
    <div>
      <MyNavbar />
      <h1>자산 관리 대시보드</h1>
      <div>
        <AssetChart />
        <StockInfo />
      </div>
    </div>
  </template>
  
  <script>
  import MyNavbar from '@/components/Navbar.vue'
  import AssetChart from '@/components/AssetChart.vue'
  import StockInfo from '@/components/StockInfo.vue'
  
  export default {
    name : 'DashboardPage',
    components: {
      MyNavbar,
      AssetChart,
      StockInfo
    }
  }
  </script>
  