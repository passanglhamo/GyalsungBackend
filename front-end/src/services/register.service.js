import axios from "axios";

// const BASE_URL = process.env.REACT_APP_BASE_URL_USER + "signup";
//const BASE_URL = process.env.REACT_APP_BASE_URL_USER + "api/user/profile/signup";
const BASE_URL = "http://localhost:8084/api/user/profile/signup";


class RegisterService {
    getCitizenDetail(cid, dob) {
        return axios.get(BASE_URL + "/getCitizenDetails"
            , {
                params: {
                    cid,
                    dob,
                }
            }
        );
    }

    receiveOtp(data) {
        return axios.post(BASE_URL + "/receiveOtp", data);
    }

    verifyOtp(data) {
        return axios.post(BASE_URL + "/verifyOtp", data);
    }
    receiveVcode(data) {
        return axios.post(BASE_URL + "/receiveEmailVcode", data);
    }

    verifyVcode(data) {
        return axios.post(BASE_URL + "/verifyEmailVcode", data);
    }
}

export default new RegisterService();
