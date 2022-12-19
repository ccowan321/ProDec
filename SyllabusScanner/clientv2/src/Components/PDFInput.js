import React, { useState } from 'react';
import * as pdfjs from 'pdfjs-dist';

function PDFInput() {
  const [pdfText, setPdfText] = useState('');

  const handlePdfChange = (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = (e) => {
      const buffer = e.target.result;
      pdfjs.getDocument(buffer).promise.then((doc) => {
        let text = '';
        const numPages = doc.numPages;

        const getPageText = (pageNum) => {
          return doc.getPage(pageNum).then((page) => {
            return page.getTextContent().then((textContent) => {
              return textContent.items.map((item) => item.str).join(' ');
            });
          });
        };

        const getAllPageText = (pageNum) => {
          if (pageNum > numPages) {
            return;
          }
          return getPageText(pageNum).then((pageText) => {
            text += `${pageText}\n`;
            return getAllPageText(pageNum + 1);
          });
        };

        return getAllPageText(1).then(() => {
          setPdfText(text);
          console.log(text); 
        });
      });
    };

    reader.readAsArrayBuffer(file);
  };

  return (
    <div>
      <input type="file" accept="application/pdf" onChange={handlePdfChange} />
      <pre>{pdfText}</pre>
    </div>
  );
}
export default PDFInput; 