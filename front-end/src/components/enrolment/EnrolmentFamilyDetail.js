import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope, faPhone, faMale, faFemale, faBriefcase, faIdCard, faFolderOpen, faTimesCircle } from "@fortawesome/free-solid-svg-icons";
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import Button from '@material-ui/core/Button';
import CancelIcon from '@material-ui/icons/Cancel';
import CancelOutlinedIcon from '@material-ui/icons/CancelOutlined';
import profileService from "../../services/profile.service";
import { useDropzone } from 'react-dropzone';

const EnrolmentFamilyDetail = (props) => {
    const { user: currentUser } = useSelector((state) => state.auth);
    const [uploadedFiles, setUploadedFiles] = useState([]);
    const [fileLimit, setFileLimit] = useState(false);
    const MAX_COUNT = 5;

    const userId = currentUser.userId;

    // for file dropzone
    const { getRootProps, getInputProps } = useDropzone({
        // accept: {
        //   'image/*': []
        // },
        maxFiles: 5,
        onDrop: acceptedFiles => {
            setUploadedFiles(acceptedFiles.map(file => Object.assign(file, {
                preview: URL.createObjectURL(file)
            })));
        }
    });

    useEffect(() => {
        // for file dropzone
        return () => uploadedFiles.forEach(file => URL.revokeObjectURL(file.preview));
    }, []);

    const handleUploadFiles = files => {
        const uploaded = [...uploadedFiles];
        let limitExceeded = false;
        files.some((file) => {
            if (uploaded.findIndex((f) => f.name === file.name) === -1) {
                uploaded.push(file);
                if (uploaded.length === MAX_COUNT) setFileLimit(true);
                if (uploaded.length > MAX_COUNT) {
                    alert(`You can only add a maximum of ${MAX_COUNT} files`);
                    setFileLimit(false);
                    limitExceeded = true;
                    return true;
                }
            }
        })
        if (!limitExceeded) setUploadedFiles(uploaded);
    }

    const handleFileEvent = (e) => {
        const chosenFiles = Array.prototype.slice.call(e.target.files)
        handleUploadFiles(chosenFiles);
    }

    const removeFile = (fileName) => {
        setUploadedFiles(current =>
            uploadedFiles.filter(file => {
                setFileLimit(false);
                return file.name !== fileName;
            }),
        );
    };


    return {
        uploadedFiles, familyDetail: (
            <div className="col-md-12 row text-muted">
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
                            <FontAwesomeIcon icon={faMale} />  Guardian's Name: </span>
                        <strong className="text-muted">{props.guardianName}</strong> <span>({props.relationToGuardian})</span>
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
                        <strong className="text-muted">{props.guardianEmail}</strong>
                    </div>
                </div>
                <div className="mb-2">
                    <hr />
                    <mark>
                        Attach family consent letter (You are required to attach consent letter because you are below 18 years on enlistment day.
                    </mark>
                </div>

                {/* with file dropzone */}

                {/* <div className='mb-2'>
                    <div {...getRootProps({ className: 'dropzone' })}>
                        <input {...getInputProps()} />
                        <CloudUploadIcon style={{ fontSize: 40 }} />
                        <div className="mb-1">Accept pdf/png/jpg files only. Must be less than 2 MB. Add upto 5 files.</div>
                        <div className="mb-1">Drag and drop some files here, or click to select files</div>

                        <div className="mb-1"> <a className={`btn btn-sm ${!fileLimit ? '' : 'disabled'} `} >
                            <FontAwesomeIcon icon={faFolderOpen} />  Browse files </a>
                        </div>
                    </div>
                </div> */}

                {/* no file dropzone */}
                <input id='fileUpload' type='file' multiple hidden
                    // accept='application/pdf, image/png'
                    onChange={handleFileEvent}
                    disabled={fileLimit}
                />
                <div className="row d-flex justify-content-between align-items-center text-muted">
                    <div className="col-auto">
                        <div className="col-md-12 p-3" style={{ border: '1.5px dashed #f5a70f', background: '#f6f1e6', textAlign: 'center' }}>
                            <label htmlFor='fileUpload'>
                                <div className={`mb-1  ${uploadedFiles.length === 0 ? 'vertical-shake' : ''} `}>
                                    <CloudUploadIcon style={{ fontSize: 40 }} />
                                </div>
                                <div className="mb-1">
                                    Accept pdf/png/jpg files only. Must be less than 2 MB. Add upto 5 files.
                                </div>
                                <div className="mb-1">
                                    <a className={`btn btn-sm ${!fileLimit ? '' : 'disabled'} `} >
                                        <FontAwesomeIcon icon={faFolderOpen} />  Browse files </a>
                                </div>
                            </label>
                        </div>
                    </div>
                    {/* <div className="col  p-2"> */}
                    <div className={`col p-2  ${uploadedFiles.length === 0 ? '' : 'bg-light animated fadeIn'} `}>
                        {uploadedFiles.length === 0 ? '' : 'You have added ' + uploadedFiles.length + ' file(s)'}
                        {uploadedFiles.map(file => (
                            <div>
                                <span className="cursor-pointer errorMsg" title="Remove this file"
                                    onClick={() => removeFile(file.name)} ><FontAwesomeIcon icon={faTimesCircle} /></span>
                                {'   '}{'   '} {file.name}{'   '} (<strong>{file.size} Bytes</strong>)
                            </div>
                        ))}
                    </div>
                </div>
            </div >
        )
    }
}

export default EnrolmentFamilyDetail;