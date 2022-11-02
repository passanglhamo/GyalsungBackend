import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import GroupIcon from '@material-ui/icons/Group';
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import profileService from "../../services/profile.service";
import { FormControl } from "@material-ui/core";


const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiFormControl-root': {
            width: '100%',
            margin: theme.spacing(1)
        }
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: '100%',
    },
    generalSetting: {
        width: '100%',
        maxWidth: 500,
    },
}));


const EditParentInfo = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);

    const [userInfo, setUserInfo] = useState([]);

    const [fatherCid, setFatherCid] = useState('');
    const [fatherOccupation, setFatherOccupation] = useState('');
    const [fatherMobileNo, setFatherMobileNo] = useState('');
    const [fatherEmail, setFatherEmail] = useState('');

    const [motherCid, setMotherCid] = useState('');
    const [motherOccupation, setMotherOccupation] = useState('');
    const [motherMobileNo, setMotherMobileNo] = useState('');
    const [motherEmail, setMotherEmail] = useState('');

    const [errorMsg, setErrorMsg] = useState('');
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);

    let userId = currentUser.userId;

    useEffect(() => {
        getProfileInfo();
    }, []);

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
                setFatherCid(response.data.fatherCid);
                setFatherOccupation(response.data.fatherOccupation);
                setFatherMobileNo(response.data.fatherMobileNo);
                setFatherEmail(response.data.fatherEmail);

                setMotherCid(response.data.motherCid);
                setMotherOccupation(response.data.motherOccupation);
                setMotherMobileNo(response.data.motherMobileNo);
                setMotherEmail(response.data.motherEmail);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    const handleSubmit = (e) => {
        setLoading(true);
        const data = {
            userId, fatherCid, fatherOccupation, fatherMobileNo, fatherEmail
            , motherCid, motherOccupation, motherMobileNo, motherEmail
        };
        profileService.changeParentInfo(data).then(
            response => {
                setSuccessfull(true);
                setLoading(false);
            },
            error => {
                setLoading(false);
                setSuccessfull(false);
                setErrorMsg(error.response.data.message);
            }
        );
    }

    return (
        <>
            <div className="col-md-12 row mb-2">
                <div className="col-md-4">
                    <Button variant="contained" color="primary" size="small" onClick={() => navigate("/gyalsupProfile")}>
                        <ChevronLeftRoundedIcon /> Go back </Button>
                </div>
            </div>
            <div className="col-md-12 row">
                <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                    <div className="col-md-10 text-muted">
                        <div className="cards">
                            <div className="card-bodys">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <GroupIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Edit parent information
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                      <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Parent information updated successfully.</strong>
                                    </div>
                                ) : (
                                    <form className={classes.root}>
                                        <div className='col-md-12 row mb-2'>
                                            <div className="col-md-6">
                                                <div className="mb-2">
                                                    <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                    </span> Father's Name: <strong className="text-muted">{userInfo.fatherName}</strong>
                                                </div>
                                                <TextField id="fatherCid" label="Father's CID" required
                                                    value={fatherCid}
                                                    onChange={(e) => { setFatherCid(e.target.value) }}
                                                />

                                                <TextField id="fatherOccupation" label="Father's Occupation" required
                                                    value={fatherOccupation}
                                                    onChange={(e) => { setFatherOccupation(e.target.value) }} />

                                                <TextField id="fatherMobileNo" label="Father's Phone" required
                                                    value={fatherMobileNo}
                                                    onChange={(e) => { setFatherMobileNo(e.target.value) }} />

                                                <TextField id="fatherEmail" label="Father's Email" required
                                                    value={fatherEmail}
                                                    onChange={(e) => { setFatherEmail(e.target.value) }} />
                                            </div>

                                            <div className="col-md-6">
                                                <div className="mb-2">
                                                    <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                    </span> Mother's Name: <strong className="text-muted">{userInfo.motherName}</strong>
                                                </div>
                                                <TextField id="motherCid" label="Mother's CID" required
                                                    value={motherCid}
                                                    onChange={(e) => { setMotherCid(e.target.value) }} />

                                                <TextField id="fatherOccupation" label="Mother's Occupation" required
                                                    value={motherOccupation}
                                                    onChange={(e) => { setMotherOccupation(e.target.value) }} />

                                                <TextField id="motherMobileNo" label="Mother's Phone" required
                                                    value={motherMobileNo}
                                                    onChange={(e) => { setMotherMobileNo(e.target.value) }} />

                                                <TextField id="motherEmail" label="Mother's Email" required
                                                    value={motherEmail}
                                                    onChange={(e) => { setMotherEmail(e.target.value) }} />
                                            </div>
                                            <div className='col-md-12 row'>
                                                <div className='d-flex flex-wrap flex-column align-items-center'>
                                                    <Button variant="contained" color="primary" onClick={handleSubmit}
                                                        disabled={loading}>
                                                        {loading && (
                                                            <span className="spinner-border spinner-border-sm"></span>
                                                        )}  Update
                                                    </Button>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        </>
    )
}

export default EditParentInfo;