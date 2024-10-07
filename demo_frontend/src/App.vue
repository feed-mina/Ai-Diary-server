<template>
  <div id="app">
    <h1>{{ message }}</h1>
    <button @click="changeMessage">메시지 변경</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: 'Hello, Vue.js!'
    };
  },
  methods: {
    changeMessage() {
      this.message = 'JavaScript로 메시지가 변경되었습니다!';
    }
  }
};
</script>

<style>
/* 간단한 스타일 */
#app {
  text-align: center;
}
</style>
