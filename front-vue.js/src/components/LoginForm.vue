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

<script setup>
import { defineEmits } from "vue";
import { useAuth } from "@/composables/useAuth";

import "@/assets/login-form.css";

const emit = defineEmits(["login-success", "login-failed"]);

const { username, password, submitLogin } = useAuth(emit);
</script>
