<template>
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
          <button @click="$emit('edit', fruit)" class="btn-edit" title="Editar fruta">
            <span>✏️</span> Editar
          </button>
          <button @click="$emit('delete', fruit.id)" class="btn-delete" title="Excluir fruta">
            <span>🗑️</span> Excluir
          </button>
        </div>
      </li>
    </ul>

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
</template>

<script>
export default {
  name: "FruitList",
  props: {
    fruitList: {
      type: Array,
      required: true
    }
  }
};
</script>
