import axios from "axios";
import authHeader from "./auth-header";
const BASE_URL = process.env.REACT_APP_BASE_URL + "api/training/management/common/getAllHospitals";
const getAll = () => {
    return axios.get(BASE_URL,
        {
            headers: authHeader()
        });
};

export default {
    getAll,
};