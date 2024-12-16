<template>
    <div id="alert">
        <h1>Stock Alert Setup</h1>
    <div>
        <label for="symbol">Stock Symbol : </label>
        <input v-model="alert.symbol" id="symbol" placeholder="Enter stock symbol" />
    </div>
    <div>
        <label for="price">Target Price : </label>
        <input v-model="alert.price" type="number" id="price" placeholder="Enter Target Price" />
    </div>
    <button @click="createAlert">Set Alert</button>

    <h2>Existing Alerts</h2>
    <ul>
        <li v-for="alert in alerts" :key="alert.id">
            {{ alert.symbol }} - Target Price : {{ alert.price }}
        </li>
    </ul>
</div>
</template>
  
  <script>
  import axios from 'axios';
  export default {
    name: 'NotFound',  // 각 컴포넌트의 이름
    data(){
        return {
            alert:{
                symbol:'',
                price:null
            },
            alerts:[]
        };
    },
    methods : {
        async createAlert(){
            if(!this.alert.symbol || !this.alert.price){
                alert('please enter both stock symbol and target price.');
                return;
            }
            try{
                const response = await axios.post('/api/alerts', this.alert);
                this.alert.push(response.data);
                this.alert.price = null;
                alert('Alert to set alert.');
            }catch(error){
                console.error('Error creating alert :', error);
                alert('Failed to set alert.');
            }
        },

        async fetchAlerts(){
            try{
                const response = await axios.get('/api/alerts');
                this.alerts =response.data;
            }catch(error){
                console.error('Error fetching alerts', error);
                alert('Faield to fetch alerts.');
            }
        },
        showBroserNotification(symbol, price){
            if(!("Notification" in window)){
                this.alert("This broswer does not support notification.");
                return;
            }

            Notification.requestPermission().then(permission =>{
                if(permission === "granted"){
                    new Notification(`price Alert : ${symbol} has reached ${price}`);
                }
            });
        }
    },
    mounted(){
        this.fetchAlerts();
        // 주기적으로 알림이 발생할 조건 체크
            setInterval(()=>{
                this.alerts.forEach(alert => {
                    // 주식 가격 조회 로직을 넣어서 특정 가격 도달시 알람을 보낸다.
                    const currentPrice = this.getCurrentPrice(alert.symbol); // api를 통해 현재 주식을 받는다.
                    if(currentPrice >= alert.price){
                        this.showBroserNotification(alert.symbol, alert.price);
                    }
            });       
        }, 60000); // 1분마다 확인 
    },
  methods : {
    getCurrentPrice(symbol){
        // 실제 주식 가격을 api를 통해 가져오는 로직 (API연동 필요)
        // 현재는 임의의 값 반환
        return Math.random()*1000;
    }
  }  
};
  </script>
  
  <style scoped>
  #alert {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  margin-top: 50px;
}
</style>
