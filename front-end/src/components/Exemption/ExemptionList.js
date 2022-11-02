import React, {useEffect, useState} from "react";
import Button from '@material-ui/core/Button';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import {Link} from '@mui/material';
import exemptionService from "../../services/exemption.service";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import TextareaAutosize from "@mui/base/TextareaAutosize";
import DialogActions from "@material-ui/core/DialogActions";
import Slide from "@material-ui/core/Slide";
import moment from "moment";
import fileDownload from "js-file-download";
import Collapse from "@material-ui/core/Collapse";
import Alert from "@material-ui/lab/Alert";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";
import reasonService from "../../services/reason.service";


const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});
const ExemptionList = () => {
    const [content, setContent] = useState('');
    const [exemptionList, setExemptionList] = useState([])
    const [open, setOpen] = useState(false);
    const [selectionModel, setSelectionModel] = useState([]);
    const [remarks, setRemarks] = useState(null)
    const [buttonValue, setButtonValue] = useState(null)
    const [selectedRowSize, setSelectedRowSize] = useState(0);
    const dateFormat = "MMMM DD, YYYY";
    const [openAttach, setOpenAttach] = useState(false);
    const [fileList, setFileList] = useState([]);
    const [responseMsg, setResponseMsg] = useState('');
    const [showAlert, setShowAlert] = useState(false);
    const [reasonList, setReasonList] = useState([]);

    const handleClose = () => {
        setOpen(false);
    };

    const handleCloseAttach = () => {
        setOpenAttach(false);
    };

    const resetPage = (resetForm) => {
        getExemptionList();
        setOpen(false);
        resetForm();
    }
    const getExemptionList = () => {
        exemptionService.getAll().then(
            response => {
                setExemptionList(response.data)
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
    };
    /*================ Get All Reasons method =============*/
    const getAllReasonList = () => {
        reasonService.getAll().then(
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
        // if (tableData.length === 0) {
        getExemptionList();
        //getUserList();
        //getTableDataList();
        //}

    }, [])

    const handleApproveOrReject = (e,resetForm) => {
        e.preventDefault();
        let exemptionIds = [];
        selectionModel.forEach(selectedData => {
            exemptionIds.push(selectedData);

        })
        if (buttonValue == 'A') {
            exemptionService.approveByIds(exemptionIds, remarks)
                .then((res) => {
                    if(res.status===208){
                        setResponseMsg(res.data);
                        setShowAlert(true);
                    }else{
                        resetPage(resetForm);
                    }

                })

        } else {
            exemptionService.rejectByIds(exemptionIds, remarks)
                .then((res) => {
                    if(res.status===208){
                        setResponseMsg(res.data);
                        setShowAlert(true);
                    }else{
                        resetPage(resetForm);
                    }

                })
        }


    };

    /*================ Method to download document=============*/
    const downloadDocument = async (cellvalue) => {

        const response = await exemptionService.downloadDocument(cellvalue.filePath);
        fileDownload(response.data, cellvalue.fileName);
    }

    const columns = [
        {
            field: 'col1',
            headerName: 'SL No',
            width: 60,
            disableClickEventBubbling: true,
            sortable: false,
            filterable: false,
            renderCell: (index) => index.api.getRowIndex(index.row.id) + 1,
        },
        {
            field: 'fullName',
            headerName: 'Name',
            width: 150,
            renderCell: (cellVal) => {
                return (
                    <Link
                        component="button"
                        variant="body2"
                        onClick={() => {
                            console.info("I'm a button.");
                        }}
                    >
                        {cellVal.value}
                    </Link>

                )
            }
        },
        {
            field: 'cid',
            headerName: 'CID',
            width: 110,
            disableClickEventBubbling: true,
        },
        {
            field: 'dob',
            headerName: 'Date of birth',
            width: 150,
            disableClickEventBubbling: true,
            valueFormatter: params =>
                moment(params?.value).format(dateFormat),
        },
        {
            field: 'sex',
            headerName: 'Gender',
            width: 100,
            disableClickEventBubbling: true,
        },
        {
            field: 'reasonId',
            headerName: 'Reason',
            width: 300,
            disableClickEventBubbling: true,
            renderCell: (cellVal) => {
                const reasonVal = reasonList.find(r => r.id == cellVal.value);

                return (
                    <div>
                        {reasonVal.name}
                    </div>
                )
            }
        },
        {
            field: 'remarks',
            headerName: 'Remarks',
            width: 200,
            disableClickEventBubbling: true,
        },
        {
            field: 'approvalRemarks',
            headerName: 'Approval Remarks',
            width: 200,
            disableClickEventBubbling: true,
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 150,
            disableClickEventBubbling: true,
            renderCell: (cellVal) => cellVal.value === 'A' ? "Approved" : (cellVal.value === 'R' ? "Rejected" : "Pending"),
        },
        {
            field: 'exemptionFileDtos',
            headerName: 'Files',
            width: 110,
            sortable: false,
            filterable: false,
            renderCell: (cellVal) => {
                const filesLen = cellVal.value.length;
                if(filesLen!==0){
                    return (

                        <div>

                            <Link
                                component="button"
                                variant="body2"
                                onClick={() => {
                                    setFileList(cellVal.value);
                                    setOpenAttach(true);
                                }}
                            >
                                {cellVal.value.length + " attachment"}
                            </Link>

                        </div>


                    )
                }
            }
        },

        // {
        //     field: 'col11',
        //     headerName: '',
        //     width: 70,
        //     renderCell: () => {
        //         return (
        //             <Button variant="contained" size="small" color="primary">
        //                 Edit
        //             </Button>
        //         )
        //     }
        // },
    ]

    return (
        <>
            <div className='col-md-12 row'>

                <div className='col-md-12 row'>
                    <div className='col-md-12 row'>
                        <div className='col-md-1'>
                            <Button variant="contained" size="small" color="primary"
                                    disabled={selectedRowSize === 0}
                                    onClick={() => {
                                        setOpen(true);
                                        setButtonValue('A')
                                    }}>Approve</Button>
                        </div>
                        <div className='col-md-4'>
                            <Button variant="contained" size="small" color="primary"
                                    disabled={selectedRowSize === 0}
                                    onClick={() => {
                                        setOpen(true);
                                        setButtonValue('R')
                                    }}
                            >Reject</Button>
                        </div>
                    </div>
                </div>

            </div>
            <div style={{height: 400, width: "100%"}}>
                <br></br>
                <DataGrid
                    rows={exemptionList}
                    columns={columns}
                    pageSize={5}
                    rowsPerPageOptions={[5, 10, 20]}
                    checkboxSelection
                    onSelectionModelChange={(newSelection) => {
                        setSelectedRowSize(newSelection.length);
                        setSelectionModel(newSelection);
                    }}
                    getRowId={(row) => row.id}
                    components={{Toolbar: GridToolbar}}
                    autoHeight
                />
                <Dialog
                    open={open}
                    TransitionComponent={Transition}
                    keepMounted
                    onClose={handleClose}
                    aria-labelledby="alert-dialog-slide-title"
                    aria-describedby="alert-dialog-slide-description"
                >
                    <DialogContent>
                        <div className="col-md-12 row">
                            <div className="col-md-12 row">
                                <TextareaAutosize minRows={3} style={{width: 500}} placeholder="Remarks"
                                                  value={remarks} onChange={e => setRemarks(e.target.value)}/>
                            </div>
                        </div>
                    </DialogContent>
                    <DialogActions>
                        <Button variant="contained" size="small" color="secondary"
                                onClick={handleClose}>
                            Cancel
                        </Button>
                        <Button variant="contained" size="small" color="primary"
                                onClick={handleApproveOrReject}>
                            {buttonValue == 'A' ? "Approve" : "Reject"}
                        </Button>
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
                </Dialog>
                <Dialog
                    open={openAttach}
                    TransitionComponent={Transition}
                    keepMounted
                    onClose={handleCloseAttach}
                    aria-labelledby="alert-dialog-slide-title"
                    aria-describedby="alert-dialog-slide-description"
                >
                    <DialogContent>
                        <div className="col-md-12 row">

                            {
                                fileList.map((file) => {
                                    return (
                                        <>
                                            <div className="col-md-12 row">
                                                <div className="col-md-4 row">
                                                    <Link
                                                        component="button"
                                                        variant="body2"
                                                        onClick={() => downloadDocument(file)}
                                                    >
                                                        {file.fileName}
                                                    </Link>

                                                </div>
                                            </div>


                                        </>

                                    )
                                })
                            }


                        </div>
                    </DialogContent>
                    <DialogActions>
                        <Button variant="contained" size="small" color="secondary"
                                onClick={handleCloseAttach}>
                            Cancel
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        </>
    )

}

export default ExemptionList