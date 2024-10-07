<template>
    <div>
      <h2>자산 변동 차트</h2>
      <BarChart :chartData="chartData" />
    </div>
  </template>
  
  <script>
  import { Bar } from 'vue-chartjs'
  import { Chart as ChartJS } from 'chart.js/auto'
  
  export default {
    components: {
      BarChart: Bar
    },
    data() {
      return {
        chartData: {
          labels: ['주식', '현금', '예금'],
          datasets: [{
            label: '자산 현황',
            data: [50000, 10000, 30000],
            backgroundColor: ['red', 'blue', 'green']
          }]
        }
      }
    }
  }
  </script>
  