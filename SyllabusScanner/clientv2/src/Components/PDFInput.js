import React, { useState } from 'react';
import * as pdfjs from 'pdfjs-dist';

function PDFInput() {
  const [pdfText, setPdfText] = useState('');
  const [file, setFile] = useState(null); 
  const [responseBody, setResponseBody] = useState(null);
  
  const handleExtractClick = (pdfText) => {
    const init = {
      method: 'POST',
      headers:{
        'Content-Type': 'application/json'
      }, 
      body: pdfText
    }
    fetch('http://localhost:8080/api', init)
    .then(resp => {
      if (resp.status===201 || resp.status === 200){
        console.log("this worked"); 
        console.log(typeof pdfText); 
        return resp.json(); 
      } 
      console.log(pdfText); 
      return Promise.reject('Something terrible has gone wrong.  Oh god the humanity!!!');
    })
    .then(response => {
      console.log(response);
      setResponseBody(response);
    })
    .catch(error => {
      console.error(error);
    });
  
  }

  const handlePdfChange = (event) => {
    const file = event.target.files[0];
    setFile(file); 
    console.log(file); 
    
    const reader = new FileReader();

    reader.onload = (e) => {
      const buffer = e.target.result;
      pdfjs.getDocument(buffer).promise.then((doc) => {
        let text = '';
        const numPages = doc.numPages;

        const getPageText = (pageNum) => {
          return doc.getPage(pageNum).then((page) => {
            return page.getTextContent().then((textContent) => {
              return textContent.items.map((item) => item.str).join('\n');
            });
          });
        };

        const getAllPageText = (pageNum) => {
          if (pageNum > numPages) {
            return;
          }
          return getPageText(pageNum).then((pageText) => {
            text += pageText;
            return getAllPageText(pageNum + 1);
          });
        };

        return getAllPageText(1).then(() => {
          setPdfText(text);
          console.log(typeof text); 
        });
      });
    };

    reader.readAsArrayBuffer(file);
  };

  return (
    <div>
      <input type="file" accept="application/pdf" onChange={handlePdfChange} />
      {file && <button onClick={() => handleExtractClick(pdfText)}>Extract Dates</button>} 
    </div>
  );
}
export default PDFInput; 