import React, {useEffect, useState} from 'react';
import {Button,Dialog,DialogActions,DialogContent,DialogTitle} from '@material-ui/core';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import {makeStyles} from '@material-ui/core/styles';
import Slide from '@material-ui/core/Slide';
import * as Yup from "yup";
import {Formik} from "formik";
import Select from "../FormsUI/Select";
import TextArea from "../FormsUI/TextArea";
import FormButton from "../FormsUI/Button";
import activationStatus from "../../data/activationStatus.json";
import fieldSpecializationService from "../../services/fieldSpecialization.service";
import isEmpty from "validator/es/lib/isEmpty";
import {Snackbar, Alert} from '@mui/material';
import Checkbox from "../FormsUI/Checkbox";

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
const FieldSpecialization = () => {

    /*================ constants value =============*/
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [fieldSpecList, setFieldSpecList] = useState([]);
    const [content, setContent] = useState('');
    const [openSuccess, setOpenSuccess] = useState(false);
    const [openSuccessUpdate, setOpenSuccessUpdate] = useState(false);
    const [defaultStatus, setDefaultStatus] = useState('A');
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
        fieldSpecName: '',
        status: defaultStatus,
        mathRequired:false,
        defaultCourse:false
    };

    /*================ form validation =============*/
    const FORM_VALIDATION = Yup.object().shape({
        fieldSpecName: Yup.string()
            .required('Field Specialization is required'),
        status: Yup.string()
            .required('Status is required'),
    });


    /*================ Get All method =============*/
    const getAll = () => {
        fieldSpecializationService.getAll().then(
            response => {
                setFieldSpecList(response.data)
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
    const submitForm = (fields, {setSubmitting, resetForm}) => {
       if (isEmpty(id.toString())) {
            create(fields, setSubmitting, resetForm);
       } else {
           update(id, fields, setSubmitting, resetForm);
        }
    }

    /*================ Create method =============*/
    function create(fields, setSubmitting, resetForm) {
        fieldSpecializationService.save(JSON.stringify(fields))
            .then(() => {
                resetPage(resetForm);
            })
            .catch(() => {
                setSubmitting(false);
            });
    }

    /*================ Update method =============*/
    function update(id, fields, setSubmitting, resetForm) {
        fieldSpecializationService.update(id, fields)
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
            width: 100,
            disableClickEventBubbling: true,
            renderCell: (index) => index.api.getRowIndex(index.row.id) + 1,
            sortable: false,
            filterable: false,
        },
        {
            field: 'fieldSpecName',
            headerName: 'Particular',
            width: 400,
        },
        {
            field: 'mathRequired',
            headerName: 'Math Required?',
            width: 150,
            renderCell:(cellVal) => cellVal.value===true?"Yes":"NO"
        },
        {
            field: 'defaultCourse',
            headerName: 'Default Course?',
            width: 150,
            renderCell:(cellVal) => cellVal.value===true?"Yes":"NO"
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 150,
            renderCell: (cellVal) => activationStatus[cellVal.value],
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
                const [fieldSpecialization, setFieldSpecialization] = useState({});
                // eslint-disable-next-line react-hooks/rules-of-hooks
                useEffect(() => {
                    if (!isEmpty(id.toString())) {
                        // get user and set form fields
                        fieldSpecializationService.getAllById(id).then(fieldVal => {
                            const fields = ['fieldSpecName', 'status','mathRequired','defaultCourse'];
                            fields.forEach(field => setFieldValue(field, fieldVal.data[field], false));
                            setFieldSpecialization(fieldVal.data);
                            values.mathRequired=fieldVal.data.mathRequired;
                            values.defaultCourse=fieldVal.data.defaultCourse;
                        });
                    }
                }, [id]);
                return (
                    <div>
                        <div className='col-md-12 row'>
                            <div className="d-flex justify-content-between align-items-center">
                                <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                    Field Specialization setup</strong>
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
                                rows={fieldSpecList}
                                columns={columns}
                                pageSize={5}
                                rowsPerPageOptions={[5]}
                                components={{Toolbar: GridToolbar}}
                                getRowId={(row) => row.id}
                                autoHeight
                            />
                            <Snackbar open={openSuccess} autoHideDuration={2000} onClose={handleCloseSuccess}
                                      anchorOrigin={{
                                          vertical: 'bottom',
                                          horizontal: 'right'
                                      }}>
                                <Alert severity="success">Data added successfully.</Alert>
                            </Snackbar>
                            <Snackbar open={openSuccessUpdate} autoHideDuration={2000}
                                      onClose={handleCloseSuccessUpdate}
                                      anchorOrigin={{
                                          vertical: 'bottom',
                                          horizontal: 'right'
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
                                <DialogTitle id="alert-dialog-slide-title">{"Field specialization"}</DialogTitle>
                                <DialogContent>

                                    <div className="col-md-12 row">
                                        <div className="col-md-12 row">
                                            <TextArea name="fieldSpecName" required placeholder="Field specialization"
                                            />
                                        </div>
                                        <div className="col-md-12 row">
                                            <Select name="status"  required label="Status" options={activationStatus}>
                                            </Select>
                                        </div>

                                        <div className="col-md-12 row">
                                            <div className="col-md-6 row">
                                                <Checkbox name="mathRequired" label="Math required?"
                                                          checkVal={values.mathRequired}/>
                                            </div>
                                            <div className="col-md-6 row">
                                                <Checkbox name="defaultCourse" label="Default Course?"
                                                          checkVal={values.defaultCourse}/>
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

export default FieldSpecialization;