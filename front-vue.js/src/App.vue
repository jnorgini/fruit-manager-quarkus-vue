<template>
  <div class="app-container">
    <div class="max-width-wrapper">
      
      <!-- Container dos Toasts na Raiz -->
      <div v-if="!isLogged" class="toast-container">
        <div v-for="toast in toasts" :key="toast.id" class="toast" :class="'toast-' + toast.type">
          <span class="toast-icon">🗑️</span>
          <span class="toast-message">{{ toast.message }}</span>
        </div>
      </div>

      <!-- Alternância Condicional das Telas -->
      <LoginForm 
        v-if="!isLogged" 
        @login-success="onLoginSuccess" 
        @login-failed="showLoginError" 
      />
      
      <!-- Escuta o evento logout e dispara o método finalizarSessao -->
      <FruitManager 
        v-else 
        :token="userToken" 
        @logout="finalizarSessao"
        @session-expired="finalizarSessao"
      />

    </div>
  </div>
</template>

<script>
import LoginForm from "./components/LoginForm.vue";
import FruitManager from "./components/FruitManager.vue";
import "./assets/main.css";

export default {
  name: "App",
  components: {
    LoginForm,
    FruitManager
  },
  data() {
    return {
      isLogged: false,
      userToken: "",
      toasts: []
    };
  },
  methods: {
    showLoginError(message) {
      const id = Date.now();
      this.toasts.push({ id, message, type: "danger" });
      setTimeout(() => {
        this.toasts = this.toasts.filter((t) => t.id !== id);
      }, 3500);
    },

    onLoginSuccess(token) {
      localStorage.setItem("user_token", token);
      this.userToken = token;
      this.isLogged = true;
    },

    finalizarSessao() {
      localStorage.removeItem("user_token");
      this.userToken = "";
      this.isLogged = false;
    }
  },
  mounted() {
    const token = localStorage.getItem("user_token");
    if (token) {
      this.userToken = token;
      this.isLogged = true;
    }
  }
};
</script>
