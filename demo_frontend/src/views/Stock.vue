<template>
    <div id="stock">
        <h1>Real-time Stock Data</h1>
        
        <div>
            <h3>Stock Information</h3>

            <label for="symbol">Stock Symbol : </label>
            <input v-model="stockSymbol" placeholder="Enter stock symbol" />
            <button @click="fetchStockData">
            Fetch Stock Data
            </button>
            <pre>{{ stockData }}</pre>
        </div>
        <div v-if="stockData">
            <h2>Stock price : {{stockData.price}}</h2>
            <canvas id="stockChart"></canvas>
        </div>
    
    </div>
</template>
  
  <script>
  import Chart from 'chart.js/auto';
  import axios from 'axios';
  export default {
    name: 'DiscountInfo',  // 각 컴포넌트의 이름
    data(){
        return {
            symbol : '',
            stockData : null,
            stockChart: null
        };
    },
    methods:{
        async fetchStockData(){
            if(!this.stockSymbol){
                alert('Please enter a stock symbol.');
                return;
            }

            try{
                const sampledata = await axios.get(`/api/stocks/${this.stockSymbol}`);
                this.stockData = sampledata.data;
                this.renderChart(this.stockData);
                
                const response = await axios.get(`https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=${this.stockSymbol}&interval=1min&apikey=your_api_key`);
                const timeSeries = response.data['Time Series (1min)'];
                const data = Object.keys(timeSeries).map(key => ({
                    time : key,
                    price : timeSeries[key]['1. open']
                }));

                this.stockData = data[0];
                this.renderChart(data);

            } catch(error){
                console.error('Error fetching stock data : ', error);
                alert('Faield to fetch stock data.');
            }
        },
        renderChart(data){
            if(this.stockChart){
                this.stockChart.destroy(); // 이전 차트를 제거
            }
            
            const ctx = document.getElementById('stockChart').getContext('2d');
            this.stockChart = new Chart(ctx, {
                type:'line',
                data : {
                    labels : data.map(item => item.time),
                    datasets : [{
                        label : `price of ${this.stockSymbol}`,
                        data : data.map(item => item.price),
                        borderColor : 'rgba(75, 192, 192, 1)',
                        borderWidth : 2,
                        fill : false
                    }]
                },
                options:{
                    scales:{
                        x:{
                            type:'time',
                            time:{
                                unit:'minute'
                            }
                        }
                    }
                }
            });

        }    
        // axios.get(`http://localhost:8080/api/stocks/${this.symbol}`)
        //     .then(response => {
        //         this.stockData = response.data;
        //     })
        //     .catch(error => {
        //         console.error("There was an error fetching the stock data!", error);
        //     })
        // }           
         
    }
  };
  </script>
  
  <style scoped>
  #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  margin-top: 50px;
}
  </style>
  