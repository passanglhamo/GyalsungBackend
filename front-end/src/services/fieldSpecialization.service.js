import axios from "axios";
import authHeader from "./auth-header";

// const BASE_URL = "http://localhost:8080/fieldSpecializations";
const BASE_URL = process.env.REACT_APP_BASE_URL + "api/training/management/fieldSpecializations";
const save = (formData) => {
    return axios.post(BASE_URL, formData,
        {
            headers: authHeader()
        }
    );
};

const getAll = () => {
    return axios.get(BASE_URL,
        {
            headers: authHeader()
        });
};

const findAllByStatus = () => {
    return axios.get(BASE_URL + "/getAllFieldSpecByStatus"
        , {
            params: {
                status: 'A'
            }
            ,headers: authHeader()
        });
};

const findAllMathRequiredCourses = () => {
    return axios.get(BASE_URL + "/getAllMathRequiredCourses",
        {
            headers: authHeader()
        }
        );
};

const findAllDefaultCourses = () => {
    return axios.get(BASE_URL + "/getAllDefaultCourses",
        {
            headers: authHeader()
        });
};

const getAllById = (id) => {
    return axios.get(BASE_URL + "/" + id,
        {
            headers: authHeader()
        });
};

const update = (id, formData) => {
    return axios.put(BASE_URL + "/" + id, formData,
        {
            headers: authHeader()
        }
    );
};

export default {
    save,
    getAll,
    getAllById,
    update,
    findAllByStatus,
    findAllMathRequiredCourses,
    findAllDefaultCourses
};