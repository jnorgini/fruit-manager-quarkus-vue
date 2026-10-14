<template>
    <section class="card" :class="{ 'card-editing': isEditing }">
        <h2 class="card-title">
            <span v-if="isEditing">✏️ Editar Fruta</span>
            <span v-else">✨ Cadastrar Nova Fruta</span>
        </h2>

        <form @submit.prevent.stop="handleSubmit" class="form-grid">
            <div class="input-group">
                <label class="label">Nome da Fruta</label>
                <input v-model="localFruit.name" type="text" placeholder="Ex: Melancia" required minlength="2"
                    maxlength="50" class="input-field" />
            </div>

            <div class="input-group">
                <label class="label">Cor Predominante</label>
                <input v-model="localFruit.color" type="text" placeholder="Ex: Verde" required minlength="2"
                    maxlength="50" class="input-field" />
            </div>

            <div class="actions-group">
                <button v-if="isEditing" type="button" @click.prevent="$emit('cancel')" class="btn-cancel">
                    Cancelar
                </button>
                <button type="submit" class="btn-submit" :class="{ 'btn-update': isEditing }">
                    <span v-if="isEditing">💾 Salvar</span>
                    <span v-else>➕ Adicionar</span>
                </button>
            </div>
        </form>
    </section>
</template>

<script>
export default {
    name: "FruitForm",
    props: {
        isEditing: Boolean,
        fruitData: Object
    },
    data() {
        return {
            localFruit: { name: "", color: "" }
        };
    },
    watch: {
        fruitData: {
            handler(newVal) {
                this.localFruit = { ...newVal };
            },
            immediate: true,
            deep: true
        }
    },
    methods: {
        handleSubmit() {
            this.$emit("submit", { ...this.localFruit });
        }
    }
};
</script>
