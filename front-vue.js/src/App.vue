<template>
  <div class="app-container">
    <div class="max-width-wrapper">

      <div v-if="!isLogged" class="toast-container">
        <div v-for="toast in toasts" :key="toast.id" class="toast" :class="'toast-' + toast.type">

          <span class="toast-icon">
            <span v-if="toast.type === 'danger'">⚠️</span>
            <span v-else>🗑️</span>
          </span>
          <span class="toast-message">{{ toast.message }}</span>
        </div>
      </div>

      <LoginForm v-if="!isLogged" @login-success="onLoginSuccess" @login-failed="showLoginError" />

      <FruitManager v-else :token="userToken" @logout="finalizarSessao" />

    </div>
  </div>
</template>

<script>
import LoginForm from "./components/LoginForm.vue";
import FruitManager from "./components/FruitManager.vue";
import axios from "axios";
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
      document.cookie = "user_token=; max-age=0; path=/; SameSite=Strict; Secure";

      const maxAge = 2 * 60 * 60; // 2 horas
      document.cookie = `user_token=${token}; max-age=${maxAge}; path=/; SameSite=Strict; Secure`;

      this.userToken = token;
      this.isLogged = true;
    },

    finalizarSessao() {
      document.cookie = "user_token=; max-age=0; path=/; SameSite=Strict; Secure";
      this.userToken = "";
      this.isLogged = false;
    },

    getCookie(name) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) return parts.pop().split(';').shift();
      return null;
    },

    configurarInterceptador() {
      const contexto = this;
      axios.interceptors.response.use(
        (response) => response,
        (error) => {

          const isLoginRequest = error.config && error.config.url && error.config.url.includes("/auth/login");

          if (isLoginRequest) {
            return Promise.reject(error);
          }

          const isAuthError = error.response && (error.response.status === 401 || error.response.status === 403);
          const isNetworkError = !error.response;

          if (isAuthError || isNetworkError) {
            contexto.finalizarSessao();

            if (isAuthError) {
              contexto.showLoginError("Sua sessão expirou por inatividade. Faça login novamente.");
            } else {
              contexto.showLoginError("A conexão com o servidor foi perdida. Faça login novamente.");
            }
          }
          return Promise.reject(error);
        }
      );
    }
  },
  created() {
    this.configurarInterceptador();
  },
  mounted() {
    const token = this.getCookie("user_token");
    if (token) {
      this.userToken = token;
      this.isLogged = true;
    }
  }
};
</script>
