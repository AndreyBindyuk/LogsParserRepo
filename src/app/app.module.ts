import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpModule} from '@angular/http';
import {FormsModule} from '@angular/forms';
import {AccordionModule} from 'ng2-bootstrap';
import {AppComponent} from './app.component';
import {LogsDataComponent} from './logs-data.component';
import {FileTreeComponent} from './file-tree.component';
import {SpinnerComponent} from './spinner.component';
import {RestService} from './rest.service';
import {Parser} from './parser';
import {MaterialModule} from '@angular/material';

@NgModule({
    imports: [
        BrowserModule,
        HttpModule,
        FormsModule,
        MaterialModule.forRoot(),
        AccordionModule.forRoot()
    ],
    declarations: [
        AppComponent,
        LogsDataComponent,
        FileTreeComponent,
        SpinnerComponent
    ],
    providers: [
        RestService,
        Parser
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
