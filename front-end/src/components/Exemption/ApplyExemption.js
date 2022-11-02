import React, {useEffect, useState} from 'react';
import Button from "../FormsUI/Button";
import {useSelector} from "react-redux";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { faFolderOpen, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {makeStyles} from "@material-ui/core/styles";
import {Formik} from "formik";
import Textfield from "../FormsUI/Textfield";
import * as Yup from "yup";
import exemptionService from '../../services/exemption.service';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import {CheckCircle} from "@material-ui/icons";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import Collapse from "@material-ui/core/Collapse";
import Alert from "@material-ui/lab/Alert";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";
import reasonService from "../../services/reason.service";
import {MenuItem} from "@material-ui/core";
import TextField from "../FormsUI/Textfield";


const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        padding: theme.spacing(2),

        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            width: '100%',
        },
        '& .MuiButtonBase-root': {
            margin: theme.spacing(2),
        },
        paper: {
            maxWidth: 500,
            margin: `${theme.spacing(1)}px auto`,
            padding: theme.spacing(2),
        },
    },
}));


const ApplyExemption = () => {
    const {user: currentUser} = useSelector((state) => state.auth);
    const [activeStep, setActiveStep] = useState(0);
    const classes = useStyles();
    const formData = new FormData();
    const [fileLimit, setFileLimit] = useState(false);
    const [uploadedFiles, setUploadedFiles] = useState([]);
    const [responseMsg, setResponseMsg] = useState('');
    const [showAlert, setShowAlert] = useState(false);
    const MAX_COUNT = 5;
    const [activeStatus, setActiveStatus] = useState('A');
    const [reasonList, setReasonList] = useState([]);
    const [content, setContent] = useState('');
    let userId = currentUser.userId;


    const uploadFileHandler = (files) => {
        const uploaded = [...uploadedFiles];

        let limitExceeded = false;
        files.some((file) => {
            if (uploaded.findIndex((f) => f.name === file.name) === -1) {
                uploaded.push(file);
                if (uploaded.length === MAX_COUNT) setFileLimit(true);
                if (uploaded.length > MAX_COUNT) {
                    alert(`You can only add maximum of ${MAX_COUNT} files`);
                    setFileLimit(false);
                    limitExceeded = true;
                    return true;
                }
            }
        })
        if (!limitExceeded) setUploadedFiles(uploaded);

    };

    const handleFileEvent = (e) => {
        const chosenFiles = Array.prototype.slice.call(e.target.files)
        uploadFileHandler(chosenFiles);
    }
    const INITIAL_FORM_STATE = {
        reasonId: '',
        proofDocuments: null,
        remarks: ''
    };

    const submitForm = ({values}) => {

        formData.append("reasonId", values.reasonId);
        formData.append("remarks", values.remarks);
        formData.append("userId", userId);
        for (let i = 0; i < uploadedFiles.length; i++) {
            formData.append('proofDocuments', uploadedFiles[i])
        }
        exemptionService.save(formData)
            .then((res) => {
                if(res.status===208){
                    setActiveStep(0);
                    setShowAlert(true);
                    setResponseMsg(res.data);
                }else{
                    setActiveStep(1);
                }
            })
            .catch(() => {
                // setShowAlert(true);
                // setSuccessful(false);
                // setLoading(false);
            });
    }
    const FORM_VALIDATION = Yup.object().shape({
        reasonId: Yup.string()
            .required('Reason is required'),
    });

    /*================ Get All Reasons method =============*/
    const getAllReasonList = () => {
        reasonService.getAllByStatus(activeStatus).then(
            response => {
                setReasonList(response.data)
            },
            error => {
                setContent(
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString()
                );
            }
        );

    }


    useEffect(() => {
        getAllReasonList();
        // for file dropzone
        return () => uploadedFiles.forEach(file => URL.revokeObjectURL(file.preview));
    }, []);

    const removeFile = (fileName) => {
        setUploadedFiles(current => uploadedFiles.filter(file => {
            setFileLimit(false);
            return file.name !== fileName;
        }),);
    };
    return (
        <>
            {activeStep === 1 ? (
                <div className='text-center alert alert-success text-success animated bounceIn'>
                    <h5><CheckCircle/> Success</h5>
                    <strong> You exemption has been successfully saved.</strong>
                    <div className=''>
                        <p>Your Exemption application will be reviewed and the outcome of the exemption will be sent
                            to you through your email.
                        </p>

                    </div>

                </div>
            ) : (
                <>
                    <Formik initialValues={{
                        ...INITIAL_FORM_STATE
                    }}
                            validationSchema={FORM_VALIDATION}
                            onSubmit={(values, {setSubmitting}) => submitForm({values, setSubmitting})}
                    >
                        {({values, setFieldValue}) => {

                            return (
                                <form className={classes.root}>
                                    {/*<Paper className={classes.paper}>*/}

                                    <div className=" text-center col-md-12  row">
                                        <div className='mb-2'>
                                            <h4><FontAwesomeIcon className="text-danger"/> Exemption </h4>
                                        </div>

                                        <div className='mb-2'>
                                            <h1><HighlightOffIcon style={{fontSize: 70}}/></h1>
                                            <strong>Following are the categories for the exemption:
                                            </strong><br></br>
                                            <small>1) monks, nuns and lay monks</small><br></br>
                                            <small>2) Medically not fit for NST</small><br></br>
                                            <hr></hr>
                                        </div>
                                        <div className=" text-center col-md-12   row">
                                            <div className=" text-center col-md-6   row">
                                                <div className=" text-center col-md-12   row">
                                                    <div className="col-md-12 row">
                                                        <input id='fileUpload' type='file' multiple hidden
                                                               onChange={handleFileEvent}
                                                               disabled={fileLimit}
                                                        />
                                                        <div className="col-auto">
                                                            <div className="col-md-12 p-3" style={{
                                                                border: '1.5px dashed #f5a70f',
                                                                background: '#f6f1e6',
                                                                textAlign: 'center'
                                                            }}>
                                                                <label htmlFor='fileUpload'>
                                                                    <div className={`mb-1  ${uploadedFiles.length === 0 ? 'vertical-shake' : ''} `}>
                                                                        <CloudUploadIcon style={{ fontSize: 40 }} />
                                                                    </div>
                                                                    <div className="mb-1">
                                                                        Accept pdf/png/jpg files only. Must be less than
                                                                        2 MB.
                                                                        Add upto 5 files.
                                                                    </div>
                                                                    <div className="mb-1">
                                                                        <a className={`btn btn-sm ${!fileLimit ? '' : 'disabled'} `}>
                                                                            <FontAwesomeIcon
                                                                                icon={faFolderOpen}/> Browse files
                                                                        </a>
                                                                    </div>
                                                                </label>
                                                                <div
                                                                    className={`col p-2  ${uploadedFiles.length === 0 ? '' : 'bg-light'} `}>
                                                                    {uploadedFiles.length === 0 ? '' : 'You have added ' + uploadedFiles.length + ' file(s)'}
                                                                    {uploadedFiles.map(file => (<div>
                                <span className="cursor-pointer errorMsg" title="Remove this file"
                                      onClick={() => removeFile(file.name)}><FontAwesomeIcon
                                    icon={faTimesCircle}/></span>
                                                                        {'   '}{'   '} {file.name}{'   '} (<strong>{file.size} Bytes</strong>)
                                                                    </div>))}
                                                                </div>
                                                            </div>
                                                        </div>


                                                    </div>
                                                </div>

                                            </div>
                                            <div className=" text-center col-md-6   row">
                                                <div className="col-md-12  row">
                                                    <TextField select={true} variant='standard' required name="reasonId"
                                                               fullWidth={true} label="Reason">
                                                        {reasonList.map((items) => {
                                                            return (
                                                                <MenuItem key={items.id}
                                                                          value={items.id}>
                                                                    {items.name}
                                                                </MenuItem>
                                                            )
                                                        })
                                                        }
                                                    </TextField>

                                                </div>
                                                <div className="col-md-12  row">
                                                    <Textfield name="remarks"  minRows={14} label="Remarks"/>
                                                </div>

                                            </div>

                                        </div>


                                        <div className="col-md-12 row">
                                            <div className="col-md-1  row">
                                                <Button type="submit" buttonName="Submit">
                                                </Button>
                                            </div>
                                        </div>
                                        <Collapse in={showAlert}>
                                            <Alert
                                                severity='error'
                                                action={
                                                    <IconButton
                                                        aria-label="close"
                                                        color="inherit"
                                                        size="small"
                                                        onClick={() => {
                                                            setShowAlert(false);
                                                        }}
                                                    >
                                                        <CloseIcon fontSize="inherit" />
                                                    </IconButton>
                                                }
                                            >
                                                {responseMsg}
                                            </Alert>
                                        </Collapse>
                                    </div>



                                </form>
                            );

                        }}
                    </Formik>
                </>)
            }

        </>

    )
}


export default ApplyExemption
