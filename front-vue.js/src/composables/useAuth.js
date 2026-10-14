import { ref } from "vue";
import { authService } from "@/services/authService";

export function useAuth(emit) {
  const username = ref("");
  const password = ref("");

  const submitLogin = () => {
    authService
      .login(username.value, password.value)
      .then((data) => {
        emit("login-success", data.token);
        username.value = "";
        password.value = "";
      })
      .catch((error) => {
        console.error("Erro no login:", error);
        emit("login-failed", "Usuário ou senha inválidos.");
      });
  };

  return {
    username,
    password,
    submitLogin,
  };
}
