<template>
  <div>
    <h2>로그인</h2>
    <form @submit.prevent="userLogin">
      <label>아이디:</label>
      <input type="text" v-model="username" />
      <label>비밀번호:</label>
      <input type="password" v-model="password" />
      <button type="submit">로그인</button>
    </form>
    <p>{{ message }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      username: "",
      password: "",
      message: ""
    };
  },
  methods: {
    async userLogin() {
      try {
        const response = await axios.post("/api/auth/userLogin", {
          username: this.username,
          password: this.password
        });
        this.message = response.data;
      } catch (error) {
        this.message = "로그인 실패!";
      }
    }
  }
};
</script>
