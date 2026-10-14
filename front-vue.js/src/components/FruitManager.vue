<template>
  <div class="app-container">
    <div class="max-width-wrapper">

      <FruitToast :toasts="toasts" />

      <header class="header" style="position: relative;">
        <button @click="deslogarSistema" class="btn-cancel"
          style="position: absolute; right: 0; top: 10px; padding: 6px 12px;">
          🚪 Sair
        </button>

        <div class="logo-badge">🍇</div>
        <h1 class="title">Gerenciador de Frutas</h1>
        <p class="subtitle">Quarkus + Vue Cloud Stack</p>
      </header>

      <FruitForm :is-editing="isEditing" :fruit-data="newFruit" @submit="handleSubmit" @cancel="cancelEdit" />

      <FruitList :fruit-list="fruitList" @edit="startEdit" @delete="deleteFruit" />

    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";
import { useFruitManager } from "@/composables/useFruitManager";
import FruitToast from "@/components/FruitToast.vue";
import FruitForm from "@/components/FruitForm.vue";
import FruitList from "@/components/FruitList.vue";

const props = defineProps(["token", "onLogout"]);
const emit = defineEmits(["logout", "session-expired"]);

const {
  fruitList,
  toasts,
  isEditing,
  newFruit,
  handleSubmit,
  cancelEdit,
  startEdit,
  deleteFruit,
  deslogarSistema
} = useFruitManager(props, emit);
</script>

<style scoped src="@/assets/fruitManager.css"></style>
