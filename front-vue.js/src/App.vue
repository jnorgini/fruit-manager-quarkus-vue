<template>
  <div class="app-container">
    <div class="max-width-wrapper">
      <!-- Container dos Toasts (Pop-ups de notificação) -->
      <div class="toast-container">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          class="toast"
          :class="'toast-' + toast.tipo"
        >
          <span class="toast-icon">
            <span v-if="toast.tipo === 'success'">✅</span>
            <span v-else-if="toast.tipo === 'danger'">🗑️</span>
            <span v-else>ℹ️</span>
          </span>
          <span class="toast-message">{{ toast.mensagem }}</span>
        </div>
      </div>

      <!-- Cabeçalho Principal -->
      <header class="header">
        <div class="logo-badge">🍇</div>
        <h1 class="title">Gerenciador de Frutas</h1>
        <p class="subtitle">Quarkus + Vue Cloud Stack</p>
      </header>

      <!-- Formulário de Cadastro / Edição -->
      <section class="card" :class="{ 'card-editing': modoEdicao }">
        <h2 class="card-title">
          <span v-if="modoEdicao">✏️ Editar Fruta</span>
          <span v-else>✨ Cadastrar Nova Fruta</span>
        </h2>

        <form @submit.prevent="submeterFormulario" class="form-grid">
          <div class="input-group">
            <label class="label">Nome da Fruta</label>
            <input
              v-model="novaFruta.nome"
              type="text"
              placeholder="Ex: Melancia"
              required
              class="input-field"
            />
          </div>

          <div class="input-group">
            <label class="label">Cor Predominante</label>
            <input
              v-model="novaFruta.cor"
              type="text"
              placeholder="Ex: Verde"
              required
              class="input-field"
            />
          </div>

          <!-- Botões Dinâmicos dependendo do Modo (Cadastro vs Edição) -->
          <div class="actions-group">
            <button
              v-if="modoEdicao"
              type="button"
              @click="cancelarEdicao"
              class="btn-cancel"
            >
              Cancelar
            </button>
            <button
              type="submit"
              class="btn-submit"
              :class="{ 'btn-update': modoEdicao }"
            >
              <span v-if="modoEdicao">💾 Salvar</span>
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
          <span class="counter-badge">{{ listaFrutas.length }} total</span>
        </div>

        <ul v-if="listaFrutas.length > 0" class="fruit-list">
          <li v-for="fruta in listaFrutas" :key="fruta.id" class="fruit-item">
            <div class="fruit-info">
              <div class="status-dot"></div>
              <div class="fruit-details">
                <span class="fruit-name">{{ fruta.nome }}</span>
                <span class="divider">•</span>
                <span class="fruit-color-badge">Cor: {{ fruta.cor }}</span>
              </div>
            </div>

            <div class="fruit-actions">
              <button
                @click="entrarModoEdicao(fruta)"
                class="btn-edit"
                title="Editar fruta"
              >
                <span>✏️</span> Editar
              </button>
              <button
                @click="deletarFruta(fruta.id)"
                class="btn-delete"
                title="Excluir fruta"
              >
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

const API_URL = "http://localhost:8080/frutas";

export default {
  name: "App",
  data() {
    return {
      listaFrutas: [],
      toasts: [], // Lista dinâmica de notificações ativas
      modoEdicao: false,
      idFrutaSendoEditada: null,
      novaFruta: {
        nome: "",
        cor: "",
      },
    };
  },
  methods: {
    // 🔍 Sistema Reativo de Alertas (Toasts)
    mostrarToast(mensagem, tipo = "success") {
      const id = Date.now();
      // Adiciona o toast na lista
      this.toasts.push({ id, mensagem, tipo });

      // Remove o toast automaticamente após 3.5 segundos
      setTimeout(() => {
        this.toasts = this.toasts.filter((t) => t.id !== id);
      }, 3500);
    },

    // 🔍 GET - Listar todas as frutas
    buscarFrutas() {
      axios
        .get(API_URL)
        .then((response) => {
          this.listaFrutas = response.data;
        })
        .catch((error) => {
          console.error("Erro ao buscar frutas do back-end:", error);
          this.mostrarToast(
            "Erro ao conectar com o servidor do Docker.",
            "danger",
          );
        });
    },

    submeterFormulario() {
      if (this.modoEdicao) {
        this.atualizarFruta();
      } else {
        this.cadastrarFruta();
      }
    },

    // ➕ POST - Cadastrar uma nova fruta
    cadastrarFruta() {
      const nomeFrutaSalva = this.novaFruta.nome;
      axios
        .post(API_URL, this.novaFruta)
        .then((response) => {
          if (Array.isArray(response.data)) {
            this.listaFrutas = response.data;
          } else {
            this.buscarFrutas();
          }
          this.mostrarToast(
            `"${nomeFrutaSalva}" cadastrada com sucesso!`,
            "success",
          );
          this.limparFormulario();
        })
        .catch((error) => {
          console.error("Erro ao cadastrar fruta:", error);
          this.mostrarToast("Não foi possível cadastrar a fruta.", "danger");
        });
    },

    entrarModoEdicao(fruta) {
      this.modoEdicao = true;
      this.idFrutaSendoEditada = fruta.id;
      this.novaFruta = {
        nome: fruta.nome,
        cor: fruta.cor,
      };
      window.scrollTo({ top: 0, behavior: "smooth" });
    },

    // 💾 PUT - Salvar alterações
    atualizarFruta() {
      axios
        .put(`${API_URL}/${this.idFrutaSendoEditada}`, this.novaFruta)
        .then((response) => {
          if (Array.isArray(response.data)) {
            this.listaFrutas = response.data;
          } else {
            this.buscarFrutas();
          }
          this.mostrarToast("Alterações salvas com sucesso!", "success");
          this.cancelarEdicao();
        })
        .catch((error) => {
          console.error("Erro ao atualizar fruta:", error);
          this.mostrarToast("Erro ao tentar atualizar os dados.", "danger");
        });
    },

    cancelarEdicao() {
      this.modoEdicao = false;
      this.idFrutaSendoEditada = null;
      this.limparFormulario();
    },

    // 🗑️ DELETE - Apagar fruta
    deletarFruta(id) {
      if (this.modoEdicao && this.idFrutaSendoEditada === id) {
        this.cancelarEdicao();
      }

      axios
        .delete(`${API_URL}/${id}`)
        .then((response) => {
          if (Array.isArray(response.data)) {
            this.listaFrutas = response.data;
          } else {
            this.buscarFrutas();
          }
          this.mostrarToast("Fruta removida da lista.", "info");
        })
        .catch((error) => {
          console.error("Erro ao deletar fruta:", error);
          this.mostrarToast("Erro ao tentar deletar a fruta.", "danger");
        });
    },

    limparFormulario() {
      this.novaFruta = { nome: "", cor: "" };
    },
  },
  mounted() {
    this.buscarFrutas();
  },
};
</script>
