# DynamicForm
Welcome to the DynamicForm wiki!

Request for getting Form details for particular RefID  Dynamic Form:
In Dynmainc forms All fields  are Case Sensitive.

"**view**": "edittext/edittextRadio/edittextCamera",

    view : It identifes that, which types of  components/fields to be display. These are following types of views.

    1.**edittext** :- it displays the editText field,where user can inputs the values.

    2.**edittextRadio**:- It displays the selection editText  field,in Which list of Options to be display ,when user will click  on that field.

    3.**edittextCamera**:- It displays the attachment editText field,in Which only images is going to be attach,when user will click that field.

"**name**": "profile photo",

       name: It is the components/fields/label's name, which is going to display above the view/field, and it must be unique.

"**hint**": "please upload your profile photo",

       hint: It is the description of the "name". If description is not there, send the field name in "hint"(bydefault send field name).

"**mandatoryOption**": "true/false",

       mandatoryOption: Components must be filled/ or skiped the field ,which is required at the time of submit the form.  

"**intputType**": "text/number/time/decimal/email",
 
        **text**:- Here inputs is only  String format(Bydefault inputType is text).
 
        **number** :- Here inputs is only  in Integer format
 
        **time** :-Here input is  only timestamp

        **decimal** :-Here input is  only decimal

        **email** :-Here input is  only email


In items :
 These are the following "Key" used for identified. All are case sensitive.

    **P** :- Positive Response (only option is required (No image and remark are required here.))
 
    **N** :- Negative Response (only option is required (No image and remark are required here.))
 
    **PR** :- Positive Response with reqiured Remarks (only option and remark are required (No image is required here))
 
    **NR** :- Negative Response with required Remarks (only option and remark are required (No image is required here))
 
    **PC** :- Positive Response with required Capture Images(only option ,remark and image are required.(all required here.))
 
    **NC** :-  Negative Response with required Capture Images (only option ,remark and image are required.(all required here.))
 
    **PI** :- only option and image are required (No remark is required)
 
    **NI** :- only option and image are required (No remark is required)

Example : 
   "items": [
                "P|OK|OK",
                "NR|Not Done|cleaning is not Done",
              ]
Removed the 3rd position in items from previous format. 
The new format is : 

1."items": [
                "P|OK",
                "NR|Not Done",
              ]

Here, There is 2 option is available.
1st Position,(i.e  NR) defines the, "selection option is a Postive or Negative" 
2nd position,(i.e Not Done) defines the " Name of the option".
3rd postion,(i.e cleaning is not Done) , After selecting the option ,this the message which will display to the user.(This part removed in new Json format)

2. "items": []
Here, There is no option is available.

Json Format:

      { 
          "DynamicForm": [
             {
              "viewID": "1",
              "view": "edittext",
              "name": "Roll Number",
              "hint": "Please Enter Your Roll Number",
              "mandatoryOption": "true",
              "intputType": "number",
              "items": []
            },
            {
              "viewID": "2",
              "view": "edittextRadio",
              "name": "Graduated or Not?",
              "hint": "Please define your Graduated?",
              "mandatoryOption": "true",
              "intputType": "text",
              "items": [
                "P|Graduated",
                "N|Not Graduated ",
                "PR|Graduated(College Name)",
                "NR|Not Graduated(+2 College Name)",
                "PC|Graduated Certificate Photo",
                "NC|Not Graduated(+2 College Photo)",
                "PI|High School Certificate Photo",
                "NI|High School Certificate Photo(+2 College Photo)"
              ]
            },
            {
              "viewID": "3",
              "view": "edittextCamera",
              "name": "All Certificate Photos",
              "hint": "All Certificate  Photos",
              "mandatoryOption": "true",
              "intputType": "text",
              "items": []
            }
          ]
        }
    
Dynamic Form Submission:

Json Format for Post :

**Syntax**:
         **"ViewID":"Value|Remarks|[Image1,image2]"**
         
 Example :
 
          {
              "FormData":{  
                  "1":"25",
                   "2":"Not OK|Remark|Images",
                   "3":"YES|Remark|NA",
                   "4":"OK|NA|NA",
                   "5":"Value|Remark|[images1,images2,image3]"
                }
           }
