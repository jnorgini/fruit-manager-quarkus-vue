import { ref, onMounted } from "vue";
import { fruitService } from "@/services/fruitService";

export function useFruitManager(props, emit) {
  const fruitList = ref([]);
  const toasts = ref([]);
  const isEditing = ref(false);
  const editingFruitId = ref(null);
  const newFruit = ref({ name: "", color: "" });

  const showToast = (message, type = "success") => {
    const id = Date.now();
    toasts.value.push({ id, message, type });
    setTimeout(() => {
      toasts.value = toasts.value.filter((t) => t.id !== id);
    }, 3500);
  };

  const clearForm = () => {
    newFruit.value = { name: "", color: "" };
  };

  const handleResponseData = (data) => {
    if (Array.isArray(data)) {
      fruitList.value = data;
    } else {
      fetchFruits();
    }
  };

  const checkAuthError = (error) => {
    if (
      error.response &&
      (error.response.status === 401 || error.response.status === 403)
    ) {
      emit("session-expired");
      return true;
    }
    return false;
  };

  const fetchFruits = () => {
    fruitService
      .getAll(props.token)
      .then((data) => {
        fruitList.value = data;
      })
      .catch((error) => {
        console.error("Erro ao buscar frutas do back-end:", error);
        if (!checkAuthError(error)) {
          showToast("Erro ao conectar com o servidor do Docker.", "danger");
        }
      });
  };

  const handleSubmit = (fruitData) => {
    if (isEditing.value) {
      updateFruit(fruitData);
    } else {
      createFruit(fruitData);
    }
  };

  const createFruit = (fruitData) => {
    const savedFruitName = fruitData.name;
    fruitService
      .create(props.token, fruitData)
      .then((data) => {
        handleResponseData(data);
        showToast(`"${savedFruitName}" cadastrada com sucesso!`, "success");
        clearForm();
      })
      .catch((error) => {
        console.error("Erro ao cadastrar fruta:", error);
        if (!checkAuthError(error)) {
          if (error.response && error.response.status === 403) {
            showToast(
              "Apenas administradores podem cadastrar frutas.",
              "danger",
            );
          } else {
            showToast("Não foi possível cadastrar a fruta.", "danger");
          }
        }
      });
  };

  const startEdit = (fruit) => {
    isEditing.value = true;
    editingFruitId.value = fruit.id;
    // Passa uma cópia limpa para evitar mutação direta antes de salvar
    newFruit.value = { name: fruit.name, color: fruit.color };
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const updateFruit = (fruitData) => {
    fruitService
      .update(props.token, editingFruitId.value, fruitData)
      .then((data) => {
        handleResponseData(data);
        showToast("Alterações salvas com sucesso!", "success");
        cancelEdit();
      })
      .catch((error) => {
        console.error("Erro ao atualizar fruta:", error);
        if (!checkAuthError(error)) {
          if (error.response && error.response.status === 403) {
            showToast("Apenas administradores podem editar frutas.", "danger");
          } else {
            showToast("Erro ao tentar atualizar os dados.", "danger");
          }
        }
      });
  };

  const cancelEdit = () => {
    isEditing.value = false;
    editingFruitId.value = null;
    clearForm();
  };

  const deleteFruit = (id) => {
    if (isEditing.value && editingFruitId.value === id) {
      cancelEdit();
    }

    fruitService
      .delete(props.token, id)
      .then((data) => {
        handleResponseData(data);
        showToast("Fruta removida da lista.", "info");
      })
      .catch((error) => {
        console.error("Erro ao deletar fruta:", error);
        if (!checkAuthError(error)) {
          if (error.response && error.response.status === 403) {
            showToast("Apenas administradores podem deletar frutas.", "danger");
          } else {
            showToast("Erro ao tentar remover a fruta.", "danger");
          }
        }
      });
  };

  const deslogarSistema = () => {
    if (typeof props.onLogout === "function") {
      props.onLogout();
    } else {
      emit("logout");
    }
  };

  onMounted(() => {
    fetchFruits();
  });

  return {
    fruitList,
    toasts,
    isEditing,
    newFruit,
    handleSubmit,
    cancelEdit,
    startEdit,
    deleteFruit,
    deslogarSistema,
  };
}
