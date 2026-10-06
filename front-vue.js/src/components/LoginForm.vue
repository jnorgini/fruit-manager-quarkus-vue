<template>
  <div class="login-wrapper">
    <header class="header">
      <div class="logo-badge">🔑</div>
      <h1 class="title">Acesso ao Sistema</h1>
      <p class="subtitle">Faça login para gerenciar as frutas</p>
    </header>

    <section class="card">
      <form @submit.prevent="submitLogin" class="login-form">
        <div class="input-group">
          <label class="label">Usuário</label>
          <input v-model="username" type="text" placeholder="Seu usuário" required class="input-field" />
        </div>

        <div class="input-group">
          <label class="label">Senha</label>
          <input v-model="password" type="password" placeholder="Sua senha" required class="input-field" />
        </div>

        <div class="actions-group">
          <button type="submit" class="btn-submit">
            <span>🔓</span> Entrar
          </button>
        </div>
      </form>
    </section>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "LoginForm",
  data() {
    return {
      username: "",
      password: ""
    };
  },
  methods: {
    submitLogin() {
      axios
        .post("http://localhost:8080/auth/login", {
          username: this.username,
          password: this.password
        })
        .then((response) => {
          this.$emit("login-success", response.data.token);
          this.username = "";
          this.password = "";
        })
        .catch((error) => {
          console.error("Erro no login:", error);
          this.$emit("login-failed", "Usuário ou senha inválidos.");
        });
    }
  }
};
</script>

<style scoped>
/* Reduz o tamanho máximo da caixinha para ficar bem centralizada e compacta */
.login-wrapper {
  max-width: 420px;
  margin: 60px auto;
  width: 100%;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
  width: 100%;
}

.actions-group {
  margin-top: 8px;
  width: 100%;
}

/* Força o botão verde a ocupar toda a largura de ponta a ponta */
.btn-submit {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 14px;
  background-color: #10b981;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.btn-submit:hover {
  background-color: #059669;
}
</style>
