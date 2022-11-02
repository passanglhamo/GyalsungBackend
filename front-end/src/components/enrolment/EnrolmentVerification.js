import React, { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useSelector } from "react-redux";
import { faEnvelope, faPhone, faMale, faFemale, faBriefcase, faIdCard, } from "@fortawesome/free-solid-svg-icons";
import Chip from '@material-ui/core/Chip';
import moment from 'moment';
import profileService from "../../services/profile.service";

const EnrolmentVerification = (props, uploadedFiles) => {
    const { user: currentUser } = useSelector((state) => state.auth);
    const [torAgreed, setTorAgreed] = useState(false);
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
        torAgreed, verification: (
            <div>
                <div className="col-md-12 row">
                    <strong className='text-muted'>Please verify following:</strong>
                </div>
                <div className="col-md-12 row">
                    <dl className="row text-muted">
                        <dt className="col-5">Name:</dt>
                        <dd className="col-7">{props.fullName}</dd>
                        <dt className="col-5">CID:</dt>
                        <dd className="col-7">{props.cid}</dd>
                        <dt className="col-5">BOD:</dt>
                        <dd className="col-7">{moment(props.dob).format('MMM MM, YYYY')}</dd>
                        <dt className="col-5">Gender:</dt>
                        <dd className="col-7">{props.sex}</dd>
                        <dt className="col-5">Mobile Number:</dt>
                        <dd className="col-7">{props.mobileNo}</dd>
                        <dt className="col-5">Email:</dt>
                        <dd className="col-7">{props.email}</dd>
                        <dt className="col-5">Village:</dt>
                        <dd className="col-7">{props.permanentPlaceName}</dd>
                        <dt className="col-5">Geog:</dt>
                        <dd className="col-7">{props.permanentGeog}</dd>
                        <dt className="col-5">Dzongkhag:</dt>
                        <dd className="col-7">{props.permanentDzongkhag}</dd>
                    </dl>
                    {/* <div className="mb-2 text-muted">
                        <FontAwesomeIcon icon={faBook} /> Studied XII Science from Tashitse HSS in 2022
                    </div> */}
                </div>
                <hr />
                <div className="col-md-12 row">
                    <div className="col-md-4">
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faMale} /> Father's Name:</span>
                            <strong className="text-muted"> {props.fatherName}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faIdCard} /> Father's CID: </span>
                            <strong className="text-muted"> {props.fatherCid}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faBriefcase} />  Father's Occupation: </span>
                            <strong className="text-muted"> {props.fatherOccupation}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faPhone} /> Father's Phone: </span>
                            <strong className="text-muted"> {props.fatherMobileNo}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faEnvelope} /> Father's Email:</span>
                            <strong className="text-muted"> {props.fatherEmail}</strong>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faFemale} /> Mother's Name:</span>
                            <strong className="text-muted"> {props.motherName}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faIdCard} /> Mother's CID:</span>
                            <strong className="text-muted"> {props.motherCid}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faBriefcase} /> Mother's Occupation: </span>
                            <strong className="text-muted"> {props.motherOccupation}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faPhone} /> Mother's Phone:</span>
                            <strong className="text-muted"> {props.motherMobileNo}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faEnvelope} /> Mother's Email:</span>
                            <strong className="text-muted"> {props.motherEmail}</strong>
                        </div>
                    </div>
                    <div className="col-4">
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faMale} /> Guardian's Name: </span>
                            <strong className="text-muted"> {props.guardianName}</strong> <span>({props.relationToGuardian})</span>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faIdCard} /> Guardian's CID: </span>
                            <strong className="text-muted"> {props.guardianCid}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faBriefcase} /> Guardian's Occupation: </span>
                            <strong className="text-muted"> {props.guardianOccupation}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faPhone} /> Guardian's Phone:</span>
                            <strong className="text-muted"> {props.guardianMobileNo}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                <FontAwesomeIcon icon={faEnvelope} /> Guardian's Email: </span>
                            <strong className="text-muted"> {props.guardianEmail}</strong>
                        </div>
                    </div>
                </div>
                <hr />
                <div className="col-md-12 p-2 bg-light text-muted" style={{ border: '1.5px dashed #f5a70f' }}>
                    <div className="mb-2">
                        <strong>Parent consent letter</strong>
                    </div>
                    {uploadedFiles.map(file => (
                        <div>
                            {file.name}{'   '} (<strong>{file.size} Bytes</strong>)
                        </div>
                    ))}
                </div>
                <hr />
                <div className="col-md-12 row">
                    <div className="mb-2">
                        <strong className="text-muted">Specialization option:</strong>
                    </div>
                    <div className="mb-2 text-muted">
                        <strong className="text-muted">Cyber security:</strong> Option 1
                    </div>
                    <div className="mb-2 text-muted">
                        <strong className="text-muted">Home security:</strong> Option 2
                    </div>
                    <div className="mb-2 text-muted">
                        <strong className="text-muted">Food security:</strong> Option 3
                    </div>
                </div>
                <hr />
                <div className="col-md-12 row">
                    <div className="mb-2">
                        <strong className="text-muted">Social media link:</strong>
                    </div>
                    <div className="mb-2">
                        <a className="text-muted text-sm-right mb-0 mb-sm-3" href={props.socialMediaLink1} target="_blank"> {props.socialMediaLink1} </a>
                    </div>
                    <div className="mb-2">
                        <a className="text-muted text-sm-right mb-0 mb-sm-3" href={props.socialMediaLink2} target="_blank"> {props.socialMediaLink2} </a>
                    </div>
                    <div className="mb-2">
                        <a className="text-muted text-sm-right mb-0 mb-sm-3" href={props.socialMediaLink3} target="_blank"> {props.socialMediaLink3} </a>
                    </div>
                </div>
                <hr />
                <div className="col-md-12 row">
                    <div className="mb-2">
                        <mark>
                            Note: If any of the information provided is mistaken
                            please go to your profile and make changes before you submit.
                        </mark>
                    </div>
                    <div className='alert alert-danger' >
                        <label>
                            <input type="checkbox" /> <strong> I declare that the information provided above is
                                accurate and true to the best of my knowledge.
                            </strong>
                        </label>
                    </div>
                </div>
            </div>
        )
    }
}

export default EnrolmentVerification;