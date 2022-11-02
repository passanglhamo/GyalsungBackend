import DefermentService from "../services/deferment.service";

export const applyDeferment = (formData) => () =>{
    return DefermentService.applyDeferment(formData).then(
    );
}

export const getAllDeferment = () => (dispatch) =>{
    return DefermentService.getAllDeferment().then(

    );
}

export const approveDeferments = (arrayIds) => (dispatch) =>{
    return DefermentService.approveDeferments(arrayIds).then(

    );
}

export const rejectDeferments = (arrayIds) => (dispatch) =>{
    return DefermentService.rejectDeferments(arrayIds).then(

    );
}

