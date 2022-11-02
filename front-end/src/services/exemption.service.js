import axios from "axios";
import multipartAuthHeader from "./auth-header-multupart";
import authHeader from "./auth-header";
// const BASE_URL = "http://localhost:8081/exemption";
const BASE_URL = process.env.REACT_APP_BASE_URL + "api/enrolment/deferment/exemption/exemption";
const save = (formData) => {
    return axios.post(BASE_URL, formData,
        {
            headers: multipartAuthHeader()
        }
    );
};

const getAll = () => {
    return axios.get(BASE_URL,
        {
            headers: authHeader()
        });
};

const approveByIds = (exemptionIds, remarks) => {
    return axios.post(BASE_URL + "/approveByIds",
        {
            exemptionIds,
            remarks,
        },
        {
            headers: authHeader()
        }
    );
};

const rejectByIds = (exemptionIds, remarks) => {
    return axios.post(BASE_URL + "/rejectByIds", {
            exemptionIds,
            remarks,
        },
        {
            headers: authHeader()
        });
};

const downloadDocument = async (url) => {
    return axios.get(BASE_URL + '/downloadFile',
        {params: {url}, responseType: 'blob', headers: authHeader()});
};


export default {
    save,
    getAll,
    approveByIds,
    rejectByIds,
    downloadDocument,
};