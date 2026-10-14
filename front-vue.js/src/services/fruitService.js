import axios from "axios";

const API_URL = "http://localhost:8080/fruits";

const getAuthConfig = (token) => ({
  headers: { Authorization: `Bearer ${token}` },
});

export const fruitService = {
  async getAll(token) {
    return axios.get(API_URL, getAuthConfig(token)).then((res) => res.data);
  },
  async create(token, fruitData) {
    return axios
      .post(API_URL, fruitData, getAuthConfig(token))
      .then((res) => res.data);
  },
  async update(token, id, fruitData) {
    return axios
      .put(`${API_URL}/${id}`, fruitData, getAuthConfig(token))
      .then((res) => res.data);
  },
  async delete(token, id) {
    return axios
      .delete(`${API_URL}/${id}`, getAuthConfig(token))
      .then((res) => res.data);
  },
};
