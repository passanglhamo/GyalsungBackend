import React, {useEffect, useState} from "react";
import Button from '@material-ui/core/Button';
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import defermentService from '../../services/deferment.service';
import {Link} from '@mui/material';
import userService from "../../services/user.service";
import moment from 'moment';
import Dialog from "@material-ui/core/Dialog";
import TextareaAutosize from '@mui/base/TextareaAutosize';
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import Slide from "@material-ui/core/Slide";
import fileDownload from 'js-file-download';
import Collapse from "@material-ui/core/Collapse";
import Alert from "@material-ui/lab/Alert";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";
import reasonService from "../../services/reason.service";

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});
const DefermentList = () => {
    /*================ constants value =============*/
    const [tableData, setTableData] = useState([])
    const [defermentList, setDefermentList] = useState([])
    const dateFormat = "MMMM DD, YYYY";
    const [selectionModel, setSelectionModel] = useState([]);
    const [pageSize, setPageSize] = useState(5);
    const [content, setContent] = useState('');
    const [open, setOpen] = useState(false);
    const [openAttach, setOpenAttach] = useState(false);
    const [selectedRowSize, setSelectedRowSize] = useState(0);
    const [remarks, setRemarks] = useState(null)
    const [buttonValue, setButtonValue] = useState(null)
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
        getDefermentList();
        setOpen(false);
        resetForm();
    }

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

    /*================ Method to get deferment list=============*/
    const getDefermentList = () => {
        defermentService.getAll().then(
            response => {
                setDefermentList(response.data)
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



    /*================Use Effect =============*/
    useEffect(() => {
        getAllReasonList();
        getDefermentList();
    }, [])

    /*================ Method to approve and reject=============*/
    const handleApproveOrReject = (e, resetForm) => {
        e.preventDefault();
        let defermentIds = [];
        selectionModel.forEach(selectedData => {
            defermentIds.push(selectedData);

        })
        if (buttonValue === 'A') {
            defermentService.approveByIds(defermentIds, remarks)
                .then((res) => {
                    if (res.status === 208) {
                        setResponseMsg(res.data);
                        setShowAlert(true);
                    } else {
                        resetPage(resetForm);
                    }


                })

        } else {
            defermentService.rejectByIds(defermentIds, remarks)
                .then((res) => {
                    if (res.status === 208) {
                        setResponseMsg(res.data);
                        setShowAlert(true);
                    } else {
                        resetPage(resetForm);
                    }
                })
        }


    };

    /*================ Method to download document=============*/
    const downloadDocument = async (cellvalue) => {

        const response = await defermentService.downloadDocument(cellvalue.filePath);
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
            width: 120,
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
            disableClickEventBubbling: true
        },
        {
            field: 'dob',
            headerName: 'Date of birth',
            width: 160,
            disableClickEventBubbling: true,
            valueFormatter: params =>
                moment(params?.value).format(dateFormat),
        },
        {
            field: 'sex',
            headerName: 'Gender',
            width: 100,
            disableClickEventBubbling: true

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
            field: 'fromDate',
            headerName: 'From Date',
            width: 200,
            disableClickEventBubbling: true,
            valueFormatter: params =>
                moment(params?.value).format(dateFormat),
        },

        {
            field: 'toDate',
            headerName: 'To Date',
            width: 160,
            disableClickEventBubbling: true,
            valueFormatter: params =>
                moment(params?.value).format(dateFormat),

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
            field: 'defermentFileDtos',
            headerName: 'Files',
            width: 110,
            sortable: false,
            filterable: false,
            renderCell: (cellVal) => {
                const filesLen = cellVal.value.length;
                if (filesLen !== 0) {
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
        {
            field: 'status',
            headerName: 'Status',
            width: 150,
            disableClickEventBubbling: true,
            renderCell: (cellVal) => cellVal.value === 'A' ? "Approved" : (cellVal.value === 'R' ? "Rejected" : "Pending"),


        },
        // {
        //     field: 'col10',

        //     headerName: '',
        //     width: 70,
        //     renderCell: () => {
        //         return (
        //             <Button variant="contained" size="small" color="primary" >
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
                        <div className='col-md-3'>
                            {/*<TextField select={true} variant='standard' fullWidth={true} label="Year">*/}
                            {/*    {Object.keys(years).map((item) => {*/}
                            {/*        return (*/}
                            {/*            <MenuItem key={item} value={item}>*/}
                            {/*                {years[item]}*/}
                            {/*            </MenuItem>*/}
                            {/*        )*/}
                            {/*    })}*/}
                            {/*</TextField>*/}

                        </div>
                        <div className='col-md-3'>
                            {/*<TextField select={true} variant='standard' fullWidth={true} label="Status">*/}
                            {/*    {Object.keys(status).map((item, pos) => {*/}
                            {/*        return (*/}
                            {/*            <MenuItem key={pos} value={item}>*/}
                            {/*                {status[item]}*/}
                            {/*            </MenuItem>*/}
                            {/*        )*/}
                            {/*    })}*/}
                            {/*</TextField>*/}

                        </div>
                        {/*<div className='col-md-2'>*/}
                        {/*    <Button variant="contained" color="primary">Search </Button>*/}
                        {/*</div>*/}
                    </div>
                    <div className='col-md-12 row'>
                        <br></br>
                        <div className='col-md-1'>
                            <Button variant="contained" size="small" color="primary"
                                    disabled={selectedRowSize === 0}
                                    onClick={() => {
                                        setOpen(true);
                                        setButtonValue('A')
                                    }}
                            >Approve</Button>
                        </div>
                        <div className='col-md-1'>
                            <Button variant="contained" size="small" color="primary" value="R"
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
                    rows={defermentList}
                    columns={columns}
                    pageSize={pageSize}
                    groupSelectsChildren={true}
                    //rowsPerPageOptions={[5, 10, 20]}
                    checkboxSelection
                    onSelectionModelChange={(newSelection) => {
                        setSelectedRowSize(newSelection.length);
                        setSelectionModel(newSelection);
                    }}
                    getRowId={(row) => row.id}
                    components={{Toolbar: GridToolbar}}
                    autoHeight
                />
            </div>
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
                                <CloseIcon fontSize="inherit"/>
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
        </>
    )

}

export default DefermentList