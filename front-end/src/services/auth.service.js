import axios from "axios";

const BASE_URL = process.env.REACT_APP_BASE_URL_USER;

const requestPasswordChange = (data) => {
  return axios.post(BASE_URL + "users/requestPasswordChange", data);
};
const validatePasswordResetLink = (requestIdFromUrl, emailFromUrl) => {
  return axios.post(BASE_URL + "users/validatePasswordResetLink", {
    requestIdFromUrl, emailFromUrl
  });
};
const resetPassword = (password, email, requestId) => {
  return axios.post(BASE_URL + "users/resetPassword", {
    password, email, requestId
  });
};

const register = (data) => {
  return axios.post(BASE_URL + "signup/signup", data);
};

const login = (username, password) => {
  return axios
    .post("http://localhost:8084/api/user/profile/signup/signin", {
      // .post(BASE_URL + "signin/signin", {
      username,
      password,
    })
    .then((response) => {
      console.log(response)
      if (response.data.accessToken) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }
      return response.data;
    });
};

const logout = () => {
  localStorage.removeItem("user");
};

export default {
  requestPasswordChange,
  validatePasswordResetLink,
  resetPassword,
  register,
  login,
  logout,
};
