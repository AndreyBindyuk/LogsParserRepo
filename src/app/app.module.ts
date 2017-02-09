import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { AccordionModule } from 'ng2-bootstrap';
import { AppComponent } from './app.component';
import { LogsDataComponent } from './logs-data.component';
import { FileTreeComponent } from './file-tree.component';
import { SpinnerComponent } from './spinner.component';
import { RestService } from './rest.service';
import { Parser } from './parser';
import { Ng2HandySyntaxHighlighterModule } from 'ng2-handy-syntax-highlighter';

@NgModule({
  imports: [
    BrowserModule,
    HttpModule,
    FormsModule,
    Ng2HandySyntaxHighlighterModule,
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
