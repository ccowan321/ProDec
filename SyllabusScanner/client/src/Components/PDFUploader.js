import React from 'react';
import { useState } from 'react';
import { Document, Page } from 'react-pdf';



function PDFUploader(){
const [pdf, setPdf] = useState(null);
const [pdfContent, setPDFContent] = useState(""); 
  

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    
    var reader = new FileReader(); 
    reader.readAsText(file)
    reader.addEventListener("load",function(){
        var pdfContent=reader.result;
        var decodedContent = atob(pdfContent)
        setPDFContent(decodedContent); 
        console.log(decodedContent); 
    })
    setPdf(file);
    
  }

  return (
    <div>
      <input type="file" accept="application/pdf" onChange={handleFileChange} />
      {pdf && <p>Selected PDF: {pdf.name}</p>}
      {pdfContent && <p>PDF Content: {pdfContent} </p>}
      
    </div>
    
  );


}

export default PDFUploader; 