import axios from "axios";

const AUTH_URL = "http://localhost:8080/auth/login";

export const authService = {
  async login(username, password) {
    return axios.post(AUTH_URL, { username, password }).then((res) => res.data);
  },
};
