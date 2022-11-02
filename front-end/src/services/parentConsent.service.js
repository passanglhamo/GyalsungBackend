import axios from "axios";
import authHeader from "./auth-header";

const BASE_URL = process.env.REACT_APP_BASE_URL;


class ParentConsentService {

    receiveOtp(data) {
        return axios.post(BASE_URL + "api/enrolment/deferment/exemption/parentConsent/receiveOtp"
            , data
            , { headers: authHeader() });
    }

    submitParentConsent(data) {
        return axios.post(BASE_URL + "api/enrolment/deferment/exemption/parentConsent/submitParentConsent"
            , data
            , { headers: authHeader() });

    }

    getParentConsentList() {
        return axios.get(BASE_URL + "api/enrolment/deferment/exemption/parentConsent/getParentConsentList"
            , { headers: authHeader() });

    }
}

export default new ParentConsentService();
