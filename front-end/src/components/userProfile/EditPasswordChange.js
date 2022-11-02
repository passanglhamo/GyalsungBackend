import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import profileService from "../../services/profile.service";
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import LockOpenOutlinedIcon from '@material-ui/icons/LockOpenOutlined';
import VpnKeyIcon from '@material-ui/icons/VpnKey';

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


const EditPasswordChange = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);
    const [currentPassword, setCurrentPassword] = useState(undefined);
    const [newPassword, setNewPassword] = useState(undefined);
    const [confirmPassword, setConfirmPassword] = useState(undefined);
    const [errorMsg, setErrorMsg] = useState('');
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);

    let userId = currentUser.userId;

    const handleChangeCurrentPassword = (e) => {
        setCurrentPassword(e.target.value);
    };

    const handleChangeNewPassword = (e) => {
        setNewPassword(e.target.value);
    };

    const handleChangeConfirmPassword = (e) => {
        setConfirmPassword(e.target.value);
    };

    const handleSubmit = (e) => {
        setLoading(true);
        const data = { userId, currentPassword, newPassword, confirmPassword };
        profileService.changePassword(data).then(
            response => {
                //show success message
                setSuccessfull(true);
                setLoading(false);
            },
            error => {
                //show error message 
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
                    <div className="col-md-8 text-muted">
                        <div className="cards">
                            <div className="card-body">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <VpnKeyIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Change password
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Your password has been changed successfully.</strong>
                                    </div>
                                ) : (
                                    <form className={classes.root}>
                                        <TextField
                                            id="standard-textarea"
                                            label="Current"
                                            required
                                            value={currentPassword}
                                            onChange={handleChangeCurrentPassword}
                                        />
                                        <TextField
                                            id="standard-textarea"
                                            label="New"
                                            required
                                            value={newPassword}
                                            onChange={handleChangeNewPassword}
                                        />
                                        <TextField
                                            id="standard-textarea"
                                            label="Re-type New"
                                            required
                                            value={confirmPassword}
                                            onChange={handleChangeConfirmPassword}
                                        />
                                        <Button variant="contained" color="primary" onClick={handleSubmit} disabled={loading}>
                                            {loading && (<span className="spinner-border spinner-border-sm"></span>)}
                                            Change Now
                                        </Button>
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

export default EditPasswordChange;