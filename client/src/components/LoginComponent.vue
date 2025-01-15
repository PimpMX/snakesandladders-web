<template>
  <div class="login-container">
    <h2>Login to Play</h2>
    <button class="btn btn-primary" @click="loginWithGoogleAccount">Sign in with Google</button>
  </div>
</template>

<script>
import { loginWithGoogle } from "@/firebase";

export default {
  name: "LoginComponent", // Updated name
  methods: {
    async loginWithGoogleAccount() {
      try {
        const user = await loginWithGoogle();
        console.log(`Welcome, ${user.displayName}`);
        this.$router.push("/"); // Redirect to the home page after login
      } catch (error) {
        console.error("Error during login:", error);
      }
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 100px;
}

button {
  font-size: 16px;
  padding: 10px 20px;
  background-color: #4285f4;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #357ae8;
}
</style>
