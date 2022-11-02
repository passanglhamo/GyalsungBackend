import axios from "axios";
import authHeader from "./auth-header";
import multipartAuthHeader from "./auth-header-multupart";

const BASE_URL = process.env.REACT_APP_BASE_URL;
// const BASE_URL = "http://localhost:81/";


class ProfileService {
    getProfilePicture(userId) {
        return axios.get(BASE_URL + "api/user/profile/userProfile/getProfilePicture"
            , { params: { userId }, headers: authHeader() });
    }

    getProfileInfo(userId) {
        return axios.get(BASE_URL + 'api/user/profile/userProfile/getProfileInfo',
            {
                params: { userId }
                , headers: authHeader()
            });
    }

    receiveOtp(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/receiveOtp", data, { headers: authHeader() });
    }

    changeMobileNo(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeMobileNo", data, { headers: authHeader() });
    }

    checkEmailExistOrNot(email) {
        return axios.get(BASE_URL + "api/user/profile/userProfile/checkEmailExistOrNot"
            , {
                params: { email }
                , headers: authHeader()
            });
    }

    receiveEmailVcode(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/receiveEmailVcode", data, { headers: authHeader() });
    }

    changeEmail(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeEmail", data, { headers: authHeader() });
    }

    changePassword(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changePassword", data, { headers: authHeader() });
    }

    changeParentInfo(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeParentInfo", data, { headers: authHeader() });
    }

    changeGuardianInfo(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeGuardianInfo", data, { headers: authHeader() });
    }

    changeSocialMediaLink(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeSocialMediaLink", data, { headers: authHeader() });
    }

    getAllDzongkhags() {
        return axios.get(BASE_URL + "api/user/profile/userProfile/getAllDzongkhags", { headers: authHeader() });
    }

    getGeogByDzongkhagId(dzongkhagId) {
        return axios.get(BASE_URL + "api/user/profile/userProfile/getGeogByDzongkhagId"
            , {
                params: {
                    dzongkhagId
                },
                headers: authHeader()
            });
    }

    changeCurrentAddress(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeCurrentAddress", data, { headers: authHeader() });
    }

    syncCensusRecord(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/syncCensusRecord", data, { headers: authHeader() });
    }

    changeUsername(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeUsername", data, { headers: authHeader() });
    }

    searchUser(searchKey) {
        return axios.get(BASE_URL + "api/user/profile/userProfile/searchUser"
            , {
                params: {
                    searchKey
                }
                , headers: authHeader()
            });
    }

    downloadFile(url) {
        return axios.get(BASE_URL + "api/user/profile/userProfile/downloadFile", { params: { url }, responseType: 'blob' }
            , { headers: authHeader() })
    }

    resetUserPassword(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/resetUserPassword", data
            , { headers: authHeader() });
    }

    changeProfilePic(data) {
        return axios.post(BASE_URL + "api/user/profile/userProfile/changeProfilePic", data
            , {
                headers: multipartAuthHeader()
            });
    }
}

export default new ProfileService();
