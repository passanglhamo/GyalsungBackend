import React, {useEffect, useState} from 'react';
import {Button,Dialog,DialogActions,makeStyles,DialogContent,DialogTitle,Slide} from '@material-ui/core';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import TextField from "../FormsUI/Textfield";
import * as Yup from "yup";
import {Formik} from "formik";
import Select from "../FormsUI/Select";
import FormButton from "../FormsUI/Button";
import Checkbox from "../FormsUI/Checkbox";
import noticeConfigurationService from "../../services/noticeConfiguration.service";
import classesList from "../../data/classes.json";
import Number from "../FormsUI/Number";
import isEmpty from "validator/es/lib/isEmpty";
import {Alert, Snackbar} from "@mui/material";

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
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: '100%',
    },
}));
const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});
const NoticeConfiguration = () => {

    /*================ constants value =============*/
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [noticeConfigServiceList, setNoticeConfigServiceList] = useState([]);
    const [content, setContent] = useState('');
    const [openSuccess, setOpenSuccess] = useState(false);
    const [openSuccessUpdate, setOpenSuccessUpdate] = useState(false);
    const [id, setId] = useState("");
    const handleClose = () => {
        setOpen(false);
    };
    const handleCloseSuccess = () => {
        setOpenSuccess(false);
    };

    const handleCloseSuccessUpdate = () => {
        setOpenSuccessUpdate(false);
    };

    const resetPage = (resetForm) => {
        setId("");
        resetForm();
        getAll();
        handleClose();
        setOpenSuccess(true);

    };
    const resetPageUpdate = (resetForm) => {
        setId("");
        resetForm();
        getAll();
        handleClose();
        setOpenSuccessUpdate(true);

    };

    /*================ Initial value for the fields =============*/
    const INITIAL_FORM_STATE = {
        noticeName: '',
        classId: '',
        age: '',
        sendSms:false,
        sendEmail:false
    };

    /*================ form validation =============*/
    const FORM_VALIDATION = Yup.object().shape({
        noticeName: Yup.string()
            .required('Notice Name is required'),
        classId: Yup.string()
            .required('Eligible to class is required'),
        age: Yup.string()
            .required('Eligible to age is required'),
    });


    /*================ Get All method =============*/
    const getAll = () => {
        noticeConfigurationService.getAll().then(
            response => {
                setNoticeConfigServiceList(response.data)
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

    /*================ use effect =============*/
    useEffect(() => {
        getAll();

    }, [])

    /*================ Submit method =============*/
    const submitForm = (fields, { setSubmitting,resetForm }) => {
        if (isEmpty(id.toString())) {
            create(fields, setSubmitting,resetForm);
        } else {
            update(id,fields, setSubmitting,resetForm);
        }
    }

    /*================ Create method =============*/
    function create(fields, setSubmitting,resetForm) {
        noticeConfigurationService.save(JSON.stringify(fields))
            .then(() => {
                resetPage(resetForm);
            })
            .catch(() => {
                setSubmitting(false);
            });
    }

    /*================ Update method =============*/
    function update(id, fields, setSubmitting,resetForm) {
        noticeConfigurationService.update(id, fields)
            .then(() => {
                resetPageUpdate(resetForm);
            })
            .catch(error => {
                setSubmitting(false);
            });
    }

    /*================Table Columns =============*/
    const columns = [
        {
            field: 'col1',
            headerName: 'SL No',
            width: 70,
            disableClickEventBubbling: true,
            renderCell: (index) => index.api.getRowIndex(index.row.id) + 1,
            sortable: false,
            filterable: false,
        },
        {
            field: 'noticeName',
            headerName: 'Notice Name',
            width: 250,
            disableClickEventBubbling: true,
        },
        {
            field: 'classId',
            headerName: 'Eligible Class',
            width: 120,
            disableClickEventBubbling: true,
            renderCell:(cellVal) => cellVal.classId
        },
        {
            field: 'age',
            headerName: 'Eligible Age',
            width: 120,
            disableClickEventBubbling: true,
            renderCell:(cellVal) => cellVal.age
        },
        {
            field: 'sendSms',
            headerName: 'Sms',
            width: 120,
            disableClickEventBubbling: true,
            renderCell:(cellVal) => cellVal.value===true?"Yes":"NO"
        },
        {
            field: 'sendEmail',
            headerName: 'Email',
            width: 120,
            disableClickEventBubbling: true,
            renderCell:(cellVal) => cellVal.value===true?"Yes":"NO"
        },
        {
            field: 'col3',
            headerName: 'Action',
            width: 100,
            disableClickEventBubbling: true,
            sortable: false,
            filterable: false,
            renderCell: (cellValue) => {
                return (
                    <Button variant="contained" size="small" color="primary" onClick={() => {
                        setId(cellValue.id);
                        setOpen(true)
                    }}>
                        Edit
                    </Button>
                )
            }
        }
    ]


    return (
        <Formik initialValues={{...INITIAL_FORM_STATE}}
                validationSchema={FORM_VALIDATION}
                onSubmit={submitForm}>
            {({values,setFieldValue}) => {
                // eslint-disable-next-line react-hooks/rules-of-hooks
                const [noticeConfiguration, setNoticeConfiguration] = useState({});
                // eslint-disable-next-line react-hooks/rules-of-hooks
                useEffect(() => {
                    if (!isEmpty(id.toString())) {
                        noticeConfigurationService.getAllById(id).then(fieldVal => {
                            const fields = ['noticeName','classId','age','sendSms','sendEmail'];
                            fields.forEach(field => setFieldValue(field, fieldVal.data[field], false));
                            setNoticeConfiguration(fieldVal.data);
                            values.sendSms=fieldVal.data.sendSms;
                            values.sendEmail=fieldVal.data.sendEmail;
                        });
                    }
                }, [id]);
                return (
                    <div>
                        <div className='col-md-12 row'>
                            <div className="d-flex justify-content-between align-items-center">
                                <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                    Notice Configuration</strong>
                                <Button variant="contained" size="small" color="primary"
                                        onClick={() => {
                                            setOpen(true)
                                            setOpenSuccess(false)
                                        }}>
                                    Add new
                                </Button>
                            </div>
                        </div>
                        <div style={{height: 300, width: "100%"}}>
                            <br></br>
                            <DataGrid
                                rows={noticeConfigServiceList}
                                columns={columns}
                                pageSize={5}
                                rowsPerPageOptions={[5]}
                                components={{Toolbar: GridToolbar}}
                                getRowId={(row) => row.id}
                                autoHeight
                            />
                            <Snackbar open={openSuccess} autoHideDuration={2000} onClose={handleCloseSuccess}
                                      anchorOrigin={{
                                          vertical:'bottom',
                                          horizontal:'right'
                                      }}>
                                <Alert severity="success">Data added successfully.</Alert>
                            </Snackbar>
                            <Snackbar open={openSuccessUpdate} autoHideDuration={2000} onClose={handleCloseSuccessUpdate}
                                      anchorOrigin={{
                                          vertical:'bottom',
                                          horizontal:'right'
                                      }}>
                                <Alert severity="success">Data updated successfully.</Alert>
                            </Snackbar>
                        </div>
                        <Dialog
                            open={open}
                            TransitionComponent={Transition}
                            keepMounted
                            onClose={handleClose}
                            aria-labelledby="alert-dialog-slide-title"
                            aria-describedby="alert-dialog-slide-description"
                        >
                            <form className={classes.root}>
                                <DialogTitle id="alert-dialog-slide-title">{"Notice Configuration"}</DialogTitle>
                                <DialogContent>

                                    <div className="col-md-12 row">
                                        <div className="col-md-12 row">
                                            <TextField name="noticeName" required label="Notice Name"/>
                                        </div>
                                        <div className="col-md-12 row">
                                            <Select name="classId" required label="Eligible to class (for students)"
                                                    options={classesList}>
                                            </Select>
                                        </div>
                                        <div className="col-md-12 row">
                                            <Number name="age" required label="Eligible to age (for school dropouts)"/>
                                        </div>
                                        <div className="col-md-12 row">
                                            <div className="col-md-6 row">
                                                <Checkbox name="sendSms" label="Send sms"
                                                          checkVal={values.sendSms}/>
                                            </div>
                                            <div className="col-md-6 row">
                                                <Checkbox name="sendEmail" label="Send email"
                                                          checkVal={values.sendEmail}/>
                                            </div>
                                        </div>
                                    </div>

                                </DialogContent>
                                <DialogActions>
                                    <Button color="secondary" variant="contained" onClick={handleClose}>
                                        Cancel
                                    </Button>
                                    <FormButton type="submit" buttonName="Submit">
                                    </FormButton>
                                </DialogActions>
                            </form>
                        </Dialog>
                    </div>

                );

            }}
        </Formik>

    )
}

export default NoticeConfiguration;