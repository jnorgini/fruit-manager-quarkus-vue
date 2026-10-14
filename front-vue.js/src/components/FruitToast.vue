<template>
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
</template>

<script>
export default {
    name: "FruitToast",
    props: {
        toasts: {
            type: Array,
            required: true
        }
    }
};
</script>
