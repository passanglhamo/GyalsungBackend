import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Provider } from "react-redux";
import store from "./store";
// import "./index.css";
// import App from "./App";
import * as serviceWorker from "./serviceWorker";
import { BrowserRouter } from "react-router-dom";


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // <React.SrictMode>
  <Provider store={store}>
    <App />
  </Provider>
  // </React.StrictMode>
);
serviceWorker.unregister();
// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();


// import React from "react";
// import ReactDOM from "react-dom";
// import { Provider } from "react-redux";
// import store from "./store";
// import "./index.css";
// import App from "./App";
// import * as serviceWorker from "./serviceWorker";

// ReactDOM.render(
//   <Provider store={store}>
//     <App />
//   </Provider>,
//   document.getElementById("root")
// );

// // If you want your app to work offline and load faster, you can chađinge
// // unregister() to register() below. Note this comes with some pitfalls.
// // Learn more about service workers: https://bit.ly/CRA-PWA
// serviceWorker.unregister();
