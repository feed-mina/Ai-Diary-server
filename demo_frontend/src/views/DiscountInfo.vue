<template>
    <div>
      <h3>Discount Information</h3>
      <button @click="fetchDiscount">할인 정보 가져오기 </button> <!-- 버튼추가-->
    <ul>
      <li v-for="(item,index) in discountItems" :key="index">
      <p><strong>이름 : </strong> {{item.name }}</p>
      <p><strong>가격 : </strong> {{ item.price }}</p>
      <p><strong>세부사항 : </strong> {{ item.details }}</p>
      <p><strong>카테고리 : </strong> {{ item.category}}</p>
      </li>
    </ul>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  const apiClient = axios.create({
    baseURL : 'http://localhost:8080/api',
    headers : {
      'Content-Type' : 'application/json',
    },
  })

export default {
    name: 'DiscountInfo',  // 각 컴포넌트의 이름
    data() {
      return {
        newDiscountItems: {
          type: '',
          amount: 0,
        },
        discountItems: [],//할인정보배열
        discountData : null, //초기값 설정
      };
    },
    mounted(){
      this.fetchDiscount();
    },
    methods: {
      async fetchDiscount() {
        try {
          const token = localStorage.getItem('jwtToken')
//          const response = await apiClient.get('/discount/getInfo');
          console.log(response.data);
        
           const response = await apiClient.get('/discount/getInfo', {
          method: 'GET',
          headers : { 
            'Authorization' : 'Bearer' + token
          },
        });
        
        if(response.status === 200){
          const discountData = await response.data;
          console.log('할인 정보 : ', discountData);

        } else{
          alert('할인 정보 가져오기 실패');
        }
        } catch (error) {
          console.error('할인정보를 가져오는 오류가 발생했습니다',error)//에러처리
          
          console.error('할인정보 가져오기 에러',error.message)//에러처리
        }

      },
      async createDiscount() {
        await axios.post('http://localhost:8080/api/discount/getInfo', this.newDiscount);
        this.fetchDiscount(); //  목록 새로 고침
      },
      async deleteDiscount(id) {
        await axios.delete(`http://localhost:8080/api/discount/getInfo/${id}`);
        this.fetchDiscount();
      },
      // async fetchDiscountPuooeteer() {
      //       // 브라우저 열기
      //     const browser = await puppeteer.launch();
      //     const page = await browser.newPage();
          
      //     // URL로 이동
      //     await page.goto('https://cu.bgfretail.com/event/plus.do?category=event');

      //     // 상품 리스트에서 정보 추출
      //     const productList = await page.evaluate(() => {
      //         const products = [];
      //         // 'li.prod_list' 요소들을 찾아서 정보 추출
      //         document.querySelectorAll('.prodListWrap ul li.prod_list').forEach(product => {
      //             const name = product.querySelector('.name p').textContent;  // 제품명
      //             const price = product.querySelector('.price strong').textContent;  // 가격
      //             const discountInfo = product.querySelector('.badge span').textContent;  // 할인 정보
      //             products.push({ name, price, discountInfo });
      //         });
      //         return products;
      //     });

      //     // 결과 출력
      //     console.log(productList);

      //     // 브라우저 닫기
      //   await browser.close();
      //   },
    },
  };
  </script>
  
  <style scoped>
  /* 필요한 스타일 */
  </style>
  