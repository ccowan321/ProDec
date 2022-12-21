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
  const handleEventNameChange = (event, index) => {
    const newValue = event.target.value;
    setResponseBody((prevResponseBody) => {
      const newResponseBody = [...prevResponseBody];
      newResponseBody[index] = {
        ...newResponseBody[index],
        eventName: newValue,
      };
      console.log(newResponseBody); 
      return newResponseBody;
    });
  };
  
  const handleDateChange = (event, index) => {
    const newValue = event.target.value;
    setResponseBody((prevResponseBody) => {
      const newResponseBody = [...prevResponseBody];
      newResponseBody[index] = {
        ...newResponseBody[index],
        localDate: newValue,
      };
      console.log(newResponseBody); 
      return newResponseBody;
    });
  };

  const handlePdfChange = (event) => {
    const file = event.target.files[0];
    setResponseBody(null);
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
      {responseBody && 
      <div style={{ height: '300px', overflow: 'auto' }}>
      {responseBody.map((item, index) => (
        <div key={index}  style={{ border: '5px solid black', margin: '10px' }}>
          <p><strong>Event Name:</strong> <input type="text" value={item.eventName}  onChange={(event) => handleEventNameChange(event, index)}></input></p>
          <p><strong>Date:</strong> <input type="date" value={item.localDate} onChange={(event) => handleDateChange(event, index)}></input></p>
      
        </div>
      ))}
    </div>}

    </div>

  );
}
export default PDFInput; 