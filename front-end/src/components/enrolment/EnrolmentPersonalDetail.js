import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope, faLocationDot, faHome, faPhone, faIdCard, faBirthdayCake, } from "@fortawesome/free-solid-svg-icons";
import moment from 'moment';
import profileService from "../../services/profile.service";


const EnrolmentPersonalDetail = (props, presentGeogName, presentDzongkhagName) => {

    const { user: currentUser } = useSelector((state) => state.auth);
    const [userInfo, setUserInfo] = useState([]);

    // let userId = currentUser.userId;

    // useEffect(() => {
    //     getProfileInfo();
    // }, []);

    // const getProfileInfo = () => {
    //     profileService.getProfileInfo(userId).then(
    //         response => {
    //             setUserInfo(response.data);
    //         },
    //         error => {
    //             console.log(
    //                 (error.response && error.response.data && error.response.data.message) ||
    //                 error.message || error.toString()
    //             );
    //         }
    //     );
    // };
    return {
        personalDetail: (
            <div>
                <div>
                    {/* <Card>
                        <CardBody> */}
                    <div className="d-flex justify-content-between align-items-center">
                        <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                            Basic information
                        </strong>
                    </div>
                    <div className="dropdown-divider"></div>
                    <div className='col-md-12 row text-muted'>
                        <div className='col-md-4'>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    Name:  </span> <strong className="text-muted">{props.fullName}</strong>

                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    <FontAwesomeIcon icon={faIdCard} /> CID:</span> <strong className="text-muted">{props.cid}</strong>
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    <FontAwesomeIcon icon={faBirthdayCake} /> BOD:</span> <strong className="text-muted">
                                    {moment(props.dob).format('MMM MM, YYYY')}</strong>
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    <FontAwesomeIcon icon={faPhone} />  Phone: </span><strong className="text-muted">{props.mobileNo}</strong>
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    <FontAwesomeIcon icon={faEnvelope} /> Email: </span><strong className="text-muted">{props.email}</strong>
                            </div>
                        </div>
                        <div className='col-md-4'>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    <FontAwesomeIcon icon={faLocationDot} /></span> Current Address
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    Lives in
                                </span>
                                <strong className="text-muted"> {props.presentPlaceName}, {presentGeogName},{presentDzongkhagName}</strong>
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    Country:
                                </span> <strong className="text-muted"> {props.presentCountry}
                                </strong>
                            </div>
                        </div>
                        <div className='col-md-4'>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    <FontAwesomeIcon icon={faHome} /></span>  Permanent Address
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    Villages:
                                </span>
                                <strong className="text-muted"> {props.permanentPlaceName}</strong>
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    Geog:
                                </span>
                                <strong className="text-muted"> {props.permanentGeog}</strong>
                            </div>
                            <div className="mb-2">
                                <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                    Dzongkhag:
                                </span>
                                <strong className="text-muted"> {props.permanentDzongkhag}</strong>
                            </div>

                        </div>
                    </div>
                    <hr />
                    {/* <div className="d-flex justify-content-between align-items-center">
                        <strong className="text-muted text-sm-left mb-0 mb-sm-3"> Education </strong>
                    </div>
                    <div className="dropdown-divider"></div>
                    <div className="mb-2 text-muted">
                        <FontAwesomeIcon icon={faBook} /> Studied XII Science from Tashitse HSS in 2022
                    </div> */}
                    <div className="dropdown-divider"></div>
                    <div className="d-flex justify-content-between align-items-center">
                        <strong className="text-muted text-sm-left mb-0 mb-sm-3"> Socal media links </strong>
                    </div>
                    <div className="dropdown-divider"></div>
                    <div className="mb-2">
                        <a className="text-muted text-sm-right mb-0 mb-sm-3" href={props.socialMediaLink1} target="_blank">{props.socialMediaLink1}</a>
                    </div>
                    <div className="mb-2">
                        <a className="text-muted text-sm-right mb-0 mb-sm-3" href={props.socialMediaLink2} target="_blank">{props.socialMediaLink2}</a>
                    </div>
                    <div className="mb-2">
                        <a className="text-muted text-sm-right mb-0 mb-sm-3" href={props.socialMediaLink3} target="_blank">{props.socialMediaLink3}</a>
                    </div>
                    {/* </CardBody>
                    </Card> */}
                </div>
            </div>
        )
    }
}

export default EnrolmentPersonalDetail;