<template>
  <div class="app-container">
    <div class="max-width-wrapper">
      <!-- Container dos Toasts (Pop-ups de notificação) -->
      <div class="toast-container">
        <div v-for="toast in toasts" :key="toast.id" class="toast" :class="'toast-' + toast.type">
          <span class="toast-icon">
            <span v-if="toast.type === 'success'">✅</span>
            <span v-else-if="toast.type === 'danger'">🗑️</span>
            <span v-else>ℹ️</span>
          </span>
          <span class="toast-message">{{ toast.message }}</span>
        </div>
      </div>

      <!-- Cabeçalho Principal -->
      <header class="header">
        <div class="logo-badge">🍇</div>
        <h1 class="title">Gerenciador de Frutas</h1>
        <p class="subtitle">Quarkus + Vue Cloud Stack</p>
      </header>

      <!-- Formulário de Cadastro / Edição -->
      <section class="card" :class="{ 'card-editing': isEditing }">
        <h2 class="card-title">
          <span v-if="isEditing">✏️ Editar Fruta</span>
          <span v-else>✨ Cadastrar Nova Fruta</span>
        </h2>

        <form @submit.prevent="handleSubmit" class="form-grid">
          <div class="input-group">
            <label class="label">Nome da Fruta</label>
            <input v-model="newFruit.name" type="text" placeholder="Ex: Melancia" required class="input-field" />
          </div>

          <div class="input-group">
            <label class="label">Cor Predominante</label>
            <input v-model="newFruit.color" type="text" placeholder="Ex: Verde" required class="input-field" />
          </div>

          <!-- Botões Dinâmicos dependendo do Modo (Cadastro vs Edição) -->
          <div class="actions-group">
            <button v-if="isEditing" type="button" @click="cancelEdit" class="btn-cancel">
              Cancelar
            </button>
            <button type="submit" class="btn-submit" :class="{ 'btn-update': isEditing }">
              <span v-if="isEditing">💾 Salvar</span>
              <span v-else>➕ Adicionar</span>
            </button>
          </div>
        </form>
      </section>

      <!-- Lista de Frutas -->
      <main class="card">
        <div class="list-header">
          <h2 class="card-title" style="margin: 0">
            📦 Frutas no Banco de Dados
          </h2>
          <span class="counter-badge">{{ fruitList.length }} total</span>
        </div>

        <ul v-if="fruitList.length > 0" class="fruit-list">
          <li v-for="fruit in fruitList" :key="fruit.id" class="fruit-item">
            <div class="fruit-info">
              <div class="status-dot"></div>
              <div class="fruit-details">
                <span class="fruit-name">{{ fruit.name }}</span>
                <span class="divider">•</span>
                <span class="fruit-color-badge">Cor: {{ fruit.color }}</span>
              </div>
            </div>

            <div class="fruit-actions">
              <button @click="startEdit(fruit)" class="btn-edit" title="Editar fruta">
                <span>✏️</span> Editar
              </button>
              <button @click="deleteFruit(fruit.id)" class="btn-delete" title="Excluir fruta">
                <span>🗑️</span> Excluir
              </button>
            </div>
          </li>
        </ul>

        <!-- Estado Vazio -->
        <div v-else class="empty-state">
          <span class="empty-icon">🍃</span>
          <p class="empty-title">
            Nenhuma fruta encontrada no banco do Docker.
          </p>
          <p class="empty-subtitle">
            Preencha o formulário acima para registrar a primeira.
          </p>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "./assets/main.css";

const API_URL = "http://localhost:8080/fruits";

export default {
  name: "App",
  data() {
    return {
      fruitList: [],
      toasts: [],
      isEditing: false,
      editingFruitId: null,
      newFruit: {
        name: "",
        color: "",
      },
    };
  },
  methods: {
    showToast(message, type = "success") {
      const id = Date.now();
      this.toasts.push({ id, message, type });
      setTimeout(() => {
        this.toasts = this.toasts.filter((t) => t.id !== id);
      }, 3500);
    },

    fetchFruits() {
      axios
        .get(API_URL)
        .then((response) => {
          this.fruitList = response.data;
        })
        .catch((error) => {
          console.error("Erro ao buscar frutas do back-end:", error);
          this.showToast(
            "Erro ao conectar com o servidor do Docker.",
            "danger",
          );
        });
    },

    handleSubmit() {
      if (this.isEditing) {
        this.updateFruit();
      } else {
        this.createFruit();
      }
    },

    createFruit() {
      const savedFruitName = this.newFruit.name;
      axios
        .post(API_URL, this.newFruit)
        .then((response) => {
          if (Array.isArray(response.data)) {
            this.fruitList = response.data;
          } else {
            this.fetchFruits();
          }
          this.showToast(
            `"${savedFruitName}" cadastrada com sucesso!`,
            "success",
          );
          this.clearForm();
        })
        .catch((error) => {
          console.error("Erro ao cadastrar fruta:", error);
          this.showToast("Não foi possível cadastrar a fruta.", "danger");
        });
    },

    startEdit(fruit) {
      this.isEditing = true;
      this.editingFruitId = fruit.id;
      this.newFruit = {
        name: fruit.name,
        color: fruit.color,
      };
      window.scrollTo({ top: 0, behavior: "smooth" });
    },

    updateFruit() {
      axios
        .put(`${API_URL}/${this.editingFruitId}`, this.newFruit)
        .then((response) => {
          if (Array.isArray(response.data)) {
            this.fruitList = response.data;
          } else {
            this.fetchFruits();
          }
          this.showToast("Alterações salvas com sucesso!", "success");
          this.cancelEdit();
        })
        .catch((error) => {
          console.error("Erro ao atualizar fruta:", error);
          this.showToast("Erro ao tentar atualizar os dados.", "danger");
        });
    },

    cancelEdit() {
      this.isEditing = false;
      this.editingFruitId = null;
      this.clearForm();
    },

    deleteFruit(id) {
      if (this.isEditing && this.editingFruitId === id) {
        this.cancelEdit();
      }

      axios
        .delete(`${API_URL}/${id}`)
        .then((response) => {
          if (Array.isArray(response.data)) {
            this.fruitList = response.data;
          } else {
            this.fetchFruits();
          }
          this.showToast("Fruta removida da lista.", "info");
        })
        .catch((error) => {
          console.error("Erro ao deletar fruta:", error);
          this.showToast("Erro ao tentar deletar a fruta.", "danger");
        });
    },

    clearForm() {
      this.newFruit = { name: "", color: "" };
    },
  },
  mounted() {
    this.fetchFruits();
  },
};
</script>
