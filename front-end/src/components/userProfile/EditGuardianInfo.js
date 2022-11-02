import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import PersonIcon from '@material-ui/icons/Person';
import profileService from "../../services/profile.service";
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';


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


const EditGuardianInfo = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);

    const [userInfo, setUserInfo] = useState([]);

    const [guardianName, setGuardianName] = useState('');
    const [guardianCid, setGuardianCid] = useState('');
    const [guardianOccupation, setGuardianOccupation] = useState('');
    const [guardianMobileNo, setGuardianMobileNo] = useState('');
    const [guardianEmail, setGuardianEmail] = useState('');
    const [relationToGuardian, setRelationToGuardian] = useState('');

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
                setGuardianName(response.data.guardianName);
                setGuardianCid(response.data.guardianCid);
                setGuardianOccupation(response.data.guardianOccupation);
                setGuardianMobileNo(response.data.guardianMobileNo);
                setGuardianEmail(response.data.guardianEmail);
                setRelationToGuardian(response.data.relationToGuardian);
            },
            error => {

                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );

        // console.log(userInfo)

    };

    const handleSubmit = (e) => {
        setLoading(true);
        const data = {
            userId, guardianName, guardianCid, guardianOccupation, guardianMobileNo, guardianEmail
            , relationToGuardian
        };
        profileService.changeGuardianInfo(data).then(
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
                    <div className="col-md-10 text-muted">
                        <div className="cards">
                            <div className="card-body">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <PersonIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Edit guardian information
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Guardian information updated successfully.</strong>
                                    </div>
                                ) : (
                                    <form className={classes.root}>
                                        <div className='col-md-12 row mb-2'>
                                            <div className='col-md-6'>
                                                <TextField id="giardianName" label="Guardian's Name" required
                                                    value={guardianName}
                                                    onChange={(e) => { setGuardianName(e.target.value) }} />
                                            </div>
                                            <div className='col-md-6'>
                                                <TextField id="giardianCid" label="Guardian's CID" required
                                                    value={guardianCid}
                                                    onChange={(e) => { setGuardianCid(e.target.value) }} />
                                            </div>
                                            <div className='col-md-6'>
                                                <TextField id="giardianOccupation" label="Guardian's Occupation" required
                                                    value={guardianOccupation}
                                                    onChange={(e) => { setGuardianOccupation(e.target.value) }} />
                                            </div>
                                            <div className='col-md-6'>
                                                <TextField id="giardianMobileNo" label="Guardian's Phone" required
                                                    value={guardianMobileNo}
                                                    onChange={(e) => { setGuardianMobileNo(e.target.value) }} />
                                            </div>
                                            <div className='col-md-6'>
                                                <TextField id="giardianEmail" label="Guardian's Email" required
                                                    value={guardianEmail}
                                                    onChange={(e) => { setGuardianEmail(e.target.value) }} />
                                            </div>
                                            <div className='col-md-6'>
                                                <TextField id="relationToGuardian" label="Relation to guardian" required
                                                    value={relationToGuardian}
                                                    onChange={(e) => { setRelationToGuardian(e.target.value) }} />
                                            </div>
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

export default EditGuardianInfo;