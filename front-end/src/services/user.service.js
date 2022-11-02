import axios from "axios";
import authHeader from "./auth-header";


const API_URL = "http://localhost:8097/api/test/";

const getRegisteredUsers = () => {
  return axios.get("http://localhost:81/api/user/profile/userProfile/getRegisteredUsers", { headers: authHeader() });
};
const getPublicContent = () => {
  return axios.get(API_URL + "all");
};

const getUserBoard = () => {
  return axios.get(API_URL + "user", { headers: authHeader() });
};

const getModeratorBoard = () => {
  return axios.get(API_URL + "mod", { headers: authHeader() });
};

const getAdminBoard = () => {
  return axios.get(API_URL + "admin", { headers: authHeader() });
};

const getRegisteredUserById = (id) => {
  // return axios.get(process.env.REACT_APP_BASE_URL + "api/user/getRegisteredUsers", { headers: authHeader() });
  return axios.get("http://localhost:8097/api/user/getRegisteredUserById", id);
};

export default {
  getRegisteredUsers,
  getPublicContent,
  getUserBoard,
  getModeratorBoard,
  getAdminBoard,
  getRegisteredUserById,
};