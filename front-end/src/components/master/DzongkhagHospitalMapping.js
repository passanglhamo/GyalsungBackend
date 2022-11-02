import React, {useEffect, useState} from 'react';
import Button from '@material-ui/core/Button';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import {makeStyles} from '@material-ui/core/styles';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle'
import Slide from '@material-ui/core/Slide';
import * as Yup from "yup";
import {Formik} from "formik";
import Select from "../FormsUI/Select";
import FormButton from "../FormsUI/Button";
import activationStatus from "../../data/activationStatus.json";
import isEmpty from "validator/es/lib/isEmpty";
import dzongkhagHospitalMapService from "../../services/dzongkhagHospitalMap.service";
import {Alert, Snackbar} from "@mui/material";
import dzongkhagService from "../../services/dzongkhag.service";
import hospitalService from "../../services/hospital.service";
import TextField from "../FormsUI/Textfield";
import {MenuItem} from "@material-ui/core";

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
const DzongkhagHospitalMapping = () => {

    /*================ constants value =============*/
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [dzongkhagHospitalMapList, setDzongkhagHospitalMapList] = useState([]);
    const [content, setContent] = useState('');
    const [id, setId] = useState("");
    const [openSuccess, setOpenSuccess] = useState(false);
    const [openSuccessUpdate, setOpenSuccessUpdate] = useState(false);
    const [dzongkhagList, setDzongkhagList] = useState([]);
    const [hospitalList, setHospitalList] = useState([]);
    const [defaultStatus, setDefaultStatus] = useState('A');


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
        dzongkhagId: '',
        hospitalId: '',
        status: defaultStatus

    };

    /*================ form validation =============*/
    const FORM_VALIDATION = Yup.object().shape({
        dzongkhagId: Yup.string()
            .required('Dzongkhag is required'),
        hospitalId: Yup.string()
            .required('Hospital is required'),
        status: Yup.string()
            .required('Status is required'),
    });
    /*================ Get All method =============*/
    const getAll = () => {
        dzongkhagHospitalMapService.getAll().then(
            response => {
                setDzongkhagHospitalMapList(response.data)
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
    /*================ Get All Dzongkhag method =============*/
    const getDzongkhagList = () => {
        dzongkhagService.getAll().then(
            response => {
                setDzongkhagList(response.data)
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

    /*================ Get All Hospital method =============*/
    const getHospitalList = () => {
        hospitalService.getAll().then(
            response => {
                setHospitalList(response.data)
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
        getDzongkhagList();
        getHospitalList();
        getAll();
    }, [])

    /*================ Submit method =============*/
    const submitForm = (fields, {setSubmitting,resetForm}) => {
        if (isEmpty(id.toString())) {
            create(fields, setSubmitting,resetForm);
        } else {
            update(id, fields, setSubmitting,resetForm);
        }
    }

    /*================ Create method =============*/
    function create(fields, setSubmitting,resetForm) {
        dzongkhagHospitalMapService.save(JSON.stringify(fields))
            .then(() => {
                resetPage(resetForm);
            })
            .catch(() => {
                setSubmitting(false);
            });
    }

    /*================ Update method =============*/
    function update(id, fields, setSubmitting,resetForm) {
        dzongkhagHospitalMapService.update(id, fields)
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
            field: 'dzongkhagId',
            headerName: 'Dzongkhag',
            width: 120,
            disableClickEventBubbling: true,
            renderCell: (cellVal) => {

                const dzoVal=dzongkhagList.find(dzo=>dzo.dzongkhagId==cellVal.value);

                return (
                    <div>
                        {dzoVal.dzongkhagName}
                    </div>
                )
            }

        },
        {
            field: 'hospitalId',
            headerName: 'Hospital',
            width: 300,
            disableClickEventBubbling: true,
            renderCell: (cellVal) => {

                const hosVal=hospitalList.find(hos=>hos.hospitalId==cellVal.value);

                return (
                    <div>
                        {hosVal.hospitalName}
                    </div>
                )
            }
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 300,
            disableClickEventBubbling: true,
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
            {({setFieldValue}) => {
                // eslint-disable-next-line react-hooks/rules-of-hooks
                const [dzongkhagHospitalMap, setDzongkhagHospitalMap] = useState({});
                // eslint-disable-next-line react-hooks/rules-of-hooks
                useEffect(() => {
                    if (!isEmpty(id.toString())) {
                        // get user and set form fields
                        dzongkhagHospitalMapService.getAllById(id).then(fieldVal => {
                            const fields = ['dzongkhagId', 'hospitalId', 'status'];
                            fields.forEach(field => setFieldValue(field, fieldVal.data[field], false));
                            setDzongkhagHospitalMap(fieldVal.data);
                        });
                    }
                }, [id]);
                return (
                    <div>
                        <div className='col-md-12 row'>
                            <div className="d-flex justify-content-between align-items-center">
                                <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                    Dzongkhag and Hospital Mapping</strong>
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
                                rows={dzongkhagHospitalMapList}
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
                                <Alert severity="success">Data mapped successfully.</Alert>
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
                                <DialogTitle id="alert-dialog-slide-title">{"Training Academy in-take"}</DialogTitle>
                                <DialogContent>

                                    <div className="col-md-12 row">
                                        <div className="col-md-12 row">
                                            <TextField select={true} variant='standard' required name="dzongkhagId"
                                                       fullWidth={true} label="Dzongkhag">
                                                {dzongkhagList.map((items) => {
                                                    return (
                                                        <MenuItem key={items.dzongkhagId}
                                                                  value={items.dzongkhagId}>
                                                            {items.dzongkhagName}
                                                        </MenuItem>
                                                    )
                                                })
                                                }
                                            </TextField>

                                        </div>
                                        <div className="col-md-12 row">
                                            <TextField select={true} variant='standard' required name="hospitalId"
                                                       fullWidth={true} label="Hospital">
                                                {hospitalList.map((items) => {
                                                    return (
                                                        <MenuItem key={items.hospitalId}
                                                                  value={items.hospitalId}>
                                                            {items.hospitalName}
                                                        </MenuItem>
                                                    )
                                                })
                                                }
                                            </TextField>

                                        </div>
                                        <div className="col-md-12 row">
                                            <Select name="status" label="Status" required options={activationStatus}>
                                            </Select>
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

export default DzongkhagHospitalMapping;