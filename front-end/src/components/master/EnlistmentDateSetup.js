import React, {useEffect, useState} from "react";
import {Button,Dialog,DialogTitle,DialogActions,Slide,DialogContent,makeStyles} from '@material-ui/core';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import * as Yup from "yup";
import {Formik} from 'formik';
import DateTimePicker from "../FormsUI/DateTimePicker";
import FormButton from "../FormsUI/Button";
import enlistmentDateService from "../../services/enlistmentDate.service";
import moment from "moment";
import Select from "../FormsUI/Select";
import activationStatus from "../../data/activationStatus.json";
import isEmpty from "validator/es/lib/isEmpty";
import {Snackbar, Alert} from '@mui/material';
import Collapse from "@material-ui/core/Collapse";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";


const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});
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
const EnlistmentDateSetup = () => {

    /*================ constants value =============*/
    const classes = useStyles();
    const [enlistmentDateList, setEnlistmentDateList] = useState([]);
    const [content, setContent] = useState('');
    const [open, setOpen] = useState(false);
    const [id, setId] = useState("");
    const [openSuccess, setOpenSuccess] = useState(false);
    const [openSuccessUpdate, setOpenSuccessUpdate] = useState(false);
    const dateFormat = "MMMM DD, YYYY";
    const [defaultStatus, setDefaultStatus] = useState('A');
    const [responseMsg, setResponseMsg] = useState('');
    const [showAlert, setShowAlert] = useState(false);


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
        fromDate: null,
        toDate: null,
        status: defaultStatus,
    };

    /*================ form validation =============*/
    const FORM_VALIDATION = Yup.object().shape({
        // fromDate: Yup.date()
        //     .required('From Date is required')
        //     .typeError("From Date is required"),
        toDate: Yup.date()
            //.required('To Date is required')
            .typeError("To Date is required")
            .min(new Date(Date.now()), "To date should be greater than current date")
            .when('fromDate',
                (fromDate, schema) => {
                    if(moment(fromDate).isValid()){
                        if (fromDate) {
                            const dayAfter = new Date(fromDate.getTime() + 86400000);

                            return schema.min(dayAfter, 'To date has to be after from date');
                        }

                        return schema;
                    }

                }),
        status: Yup.string()
            .required('Status is required'),
    });

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
        enlistmentDateService.save(fields)
            .then((res) => {
                if(res.status===208){
                    setShowAlert(true);
                    setResponseMsg(res.data);
                }else{
                    resetPage(resetForm);
                }


            })
            .catch(() => {
                setSubmitting(false);
            });
    }

    /*================ Update method =============*/
    function update(id, fields, setSubmitting, resetForm) {
        enlistmentDateService.update(id, fields)
            .then(() => {
                resetPageUpdate(resetForm);
            })
            .catch(error => {
                setSubmitting(false);
            });
    }

    /*================ Get All method =============*/
    const getAll = () => {
        enlistmentDateService.getAll().then(
            response => {
                setEnlistmentDateList(response.data)
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
        )


    }
    useEffect(() => {
        getAll();
    }, [])

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
            field: 'fromDate',
            headerName: 'From Date',
            width: 250,
            disableClickEventBubbling: true,
            valueFormatter: params =>
                moment(params?.value).format(dateFormat),
        },
        {
            field: 'toDate',
            headerName: 'To Date',
            width: 250,
            disableClickEventBubbling: true,
            valueFormatter: params =>
                moment(params?.value).format(dateFormat),
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 250,
            disableClickEventBubbling: true,
            renderCell: (cellVal) => activationStatus[cellVal.value],

        },
        {
            field: 'col5',
            headerName: '',
            width: 80,
            sortable: false,
            filterable: false,
            renderCell: (cellValue) => {
                return (
                    <Button variant="contained" color="primary"
                            onClick={() => {
                                setId(cellValue.id);
                                setOpen(true)
                            }}
                    > Edit </Button>
                )
            }
        },
    ]

    return (
        <Formik initialValues={{...INITIAL_FORM_STATE}}
                validationSchema={FORM_VALIDATION}
                onSubmit={submitForm}>
            {({values, setFieldValue}) => {
                // eslint-disable-next-line react-hooks/rules-of-hooks
                const [fieldSpecialization, setFieldSpecialization] = useState({});
                // eslint-disable-next-line react-hooks/rules-of-hooks
                useEffect(() => {
                    if (!isEmpty(id.toString())) {
                        // get user and set form fields
                        enlistmentDateService.getAllById(id).then(fieldVal => {
                            const fields = ['fromDate', 'toDate', 'status'];
                            fields.forEach(field => setFieldValue(field, fieldVal.data[field], false));
                            setFieldSpecialization(fieldVal.data);
                        });
                    }
                }, [id]);
                return (
                    <div>
                        <div className='col-md-12 row'>
                            <div className="d-flex justify-content-between align-items-center">
                                <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                    Enlistment Date Setup</strong>
                                <Button variant="contained" size="small" color="primary"
                                        onClick={() => {
                                            setOpen(true)
                                        }}>
                                    Add new
                                </Button>
                            </div>
                        </div>
                        <div style={{height: 300, width: "100%"}}>
                            <br></br>
                            <DataGrid
                                rows={enlistmentDateList}
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
                                <Alert severity="success">Enlistment Date added successfully.</Alert>
                            </Snackbar>
                            <Snackbar open={openSuccessUpdate} autoHideDuration={2000}
                                      onClose={handleCloseSuccessUpdate}
                                      anchorOrigin={{
                                          vertical: 'bottom',
                                          horizontal: 'right'
                                      }}>
                                <Alert severity="success">Enlistment Date updated successfully.</Alert>
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
                            <form className={classes.root} onSubmit={submitForm}>
                                <DialogTitle id="alert-dialog-slide-title">{"Enlistment Date Setup"}</DialogTitle>
                                <DialogContent>

                                    <div className="col-md-12 row">
                                        <DateTimePicker name="fromDate" label="From Date"
                                                        InputProps={{
                                                            style: {
                                                                height: 44
                                                            }
                                                        }}
                                                        selected={values.fromDate}
                                                        onChange={date => setFieldValue('fromDate', date)}
                                        />
                                    </div>


                                    <div className="col-md-12 row">
                                        <DateTimePicker name="toDate" label="To Date"
                                                        InputProps={{
                                                            style: {
                                                                height: 44
                                                            }
                                                        }}
                                                        selected={values.toDate}
                                                        onChange={date => setFieldValue('toDate', date)}
                                        />

                                    </div>

                                    <div className="col-md-12 row">
                                        <Select name="status" required label="Status" options={activationStatus}>
                                        </Select>
                                    </div>

                                </DialogContent>
                                <DialogActions>
                                    <Button color="secondary" variant="contained" onClick={handleClose}>
                                        Cancel
                                    </Button>
                                    <FormButton type="submit" buttonName="Submit">
                                    </FormButton>
                                </DialogActions>
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
                            </form>
                        </Dialog>
                    </div>

                );

            }}
        </Formik>

    )

}

export default EnlistmentDateSetup