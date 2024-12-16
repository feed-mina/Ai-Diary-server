<template>
    <div>
      <h1>자산 관리</h1>
      <form @submit.prevent="createAsset">
        <input v-model="newAsset.type" placeholder="자산 종류" />
        <input v-model="newAsset.amount" placeholder="금액" type="number" />
        <button type="submit">자산 등록</button>
      </form>
  
      <ul>
        <li v-for="asset in assets" :key="asset.id">
          {{ asset.type }}: {{ asset.amount }} 원
          <button @click="deleteAsset(asset.id)">삭제</button>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name : 'AssetPage',
    data() {
      return {
        newAsset: {
          type: '',
          amount: 0,
        },
        assets: [],
      };
    },
    methods: {
      async fetchAssets() {
        const response = await axios.get('http://localhost:8080/assets');
        this.assets = response.data;
      },
      async createAsset() {
        await axios.post('http://localhost:8080/assets', this.newAsset);
        this.fetchAssets(); // 자산 목록 새로 고침
      },
      async deleteAsset(id) {
        await axios.delete(`http://localhost:8080/assets/${id}`);
        this.fetchAssets();
      },
    },
    mounted() {
      this.fetchAssets();
    },
  };
  </script>
  